package paiva.thiago.news.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import okio.IOException
import paiva.thiago.news.BuildConfig
import paiva.thiago.news.data.api.NewsApi
import paiva.thiago.news.data.database.NewsDatabase
import paiva.thiago.news.data.database.entity.ArticleEntity
import paiva.thiago.news.data.database.entity.RemoteKeys
import paiva.thiago.news.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDatabase: NewsDatabase
) : RemoteMediator<Int, ArticleEntity>() {

    private val articleDao = newsDatabase.articleDao()
    private val remoteKeysDao = newsDatabase.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val closestToCurrentPosition = state.anchorPosition?.let { state.closestItemToPosition(it) }
                closestToCurrentPosition?.let {
                    remoteKeysDao.remoteKeysByArticleTitle(it.title)?.nextKey?.minus(1)
                } ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = newsApi.getTopHeadlines(
                apiKey = BuildConfig.API_KEY,
                source = BuildConfig.NEWS_SOURCE,
                pageSize = state.config.pageSize,
                page = page
            )

            val articles = response.articles.map { it.toArticle() }
            val endOfPaginationReached = articles.isEmpty()

            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    articleDao.clearAll()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(articleTitle = it.title, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insertAll(keys)
                articleDao.insertAll(articles.map { ArticleEntity.fromArticle(it) })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            Log.e("NewsRemoteMediator", "Network error: ${exception.localizedMessage}")
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e("NewsRemoteMediator", "HTTP error: ${exception.localizedMessage}")
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleEntity>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { article -> newsDatabase.remoteKeysDao().remoteKeysByArticleTitle(article.title) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleEntity>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { article -> newsDatabase.remoteKeysDao().remoteKeysByArticleTitle(article.title) }
    }
}