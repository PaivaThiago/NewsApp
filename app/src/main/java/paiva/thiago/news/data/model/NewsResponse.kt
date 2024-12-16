package paiva.thiago.news.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val articles: List<ArticleResponse>
)