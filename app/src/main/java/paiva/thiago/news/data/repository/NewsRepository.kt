package paiva.thiago.news.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import paiva.thiago.news.data.database.dao.ArticleDAO
import paiva.thiago.news.data.mediator.NewsRemoteMediator
import paiva.thiago.news.data.model.Article
import paiva.thiago.news.utils.PAGE_SIZE
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val articleDao: ArticleDAO,
    private val remoteMediator: NewsRemoteMediator
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPaginatedArticles(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { articleDao.pagingSource() }
        ).flow.map { pagingData -> pagingData.map { entity -> entity.toArticle() } }
    }
}