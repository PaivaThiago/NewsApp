package paiva.thiago.news.data.api

import paiva.thiago.news.data.model.NewsResponse
import paiva.thiago.news.utils.ENDPOINT_TOP_HEADLINES
import paiva.thiago.news.utils.QUERY_PARAM_API_KEY
import paiva.thiago.news.utils.QUERY_PARAM_PAGE
import paiva.thiago.news.utils.QUERY_PARAM_PAGE_SIZE
import paiva.thiago.news.utils.QUERY_PARAM_SOURCES
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET(ENDPOINT_TOP_HEADLINES)
    suspend fun getTopHeadlines(
        @Query(QUERY_PARAM_API_KEY) apiKey: String,
        @Query(QUERY_PARAM_SOURCES) source: String,
        @Query(QUERY_PARAM_PAGE_SIZE) pageSize: Int,
        @Query(QUERY_PARAM_PAGE) page: Int
    ): NewsResponse
}