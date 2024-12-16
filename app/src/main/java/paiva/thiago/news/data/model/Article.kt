package paiva.thiago.news.data.model

import paiva.thiago.news.utils.DATE_PATTERN
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Article(
    val title: String,
    val imageUrl: String?,
    val date: ZonedDateTime,
    val description: String?,
    val content: String?
) {
    val dateFormatted: String
        get() = date.format(DateTimeFormatter.ofPattern(DATE_PATTERN))
}