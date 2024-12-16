package paiva.thiago.news.data.model

import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class ArticleResponse(
    val title: String,
    val urlToImage: String?,
    val publishedAt: String,
    val description: String?,
    val content: String?
) {
    fun toArticle(): Article = Article(
        title = title,
        imageUrl = urlToImage,
        date = ZonedDateTime.parse(publishedAt),
        description = description,
        content = content
    )
}