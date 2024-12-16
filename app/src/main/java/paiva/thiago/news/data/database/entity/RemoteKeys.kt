package paiva.thiago.news.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val articleTitle: String,
    val prevKey: Int?,
    val nextKey: Int?
)