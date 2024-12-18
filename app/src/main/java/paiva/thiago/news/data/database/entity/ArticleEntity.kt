package paiva.thiago.news.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import paiva.thiago.news.data.model.Article
import paiva.thiago.news.data.model.ArticleResponse
import paiva.thiago.news.utils.TABLE_ARTICLES
import java.time.ZonedDateTime

@Entity(tableName = TABLE_ARTICLES)
data class ArticleEntity(
    @PrimaryKey val title: String,
    val imageUrl: String?,
    val date: String,
    val description: String?,
    val content: String?
) {
    fun toArticle(): Article = Article(
        title = title,
        imageUrl = imageUrl,
        date = ZonedDateTime.parse(date),
        description = description,
        content = content
    )

    fun toArticleResponses(): ArticleResponse = ArticleResponse(
        title = title,
        urlToImage = imageUrl,
        publishedAt = ZonedDateTime.parse(date).toString(),
        description = description,
        content = content
    )

    companion object {
        fun fromArticle(article: Article): ArticleEntity = ArticleEntity(
            title = article.title,
            imageUrl = article.imageUrl,
            date = article.date.toString(),
            description = article.description,
            content = article.content
        )
    }
}
