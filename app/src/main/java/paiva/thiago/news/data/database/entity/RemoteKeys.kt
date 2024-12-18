package paiva.thiago.news.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import paiva.thiago.news.utils.TABLE_REMOTE_KEYS

@Entity(tableName = TABLE_REMOTE_KEYS)
data class RemoteKeys(
    @PrimaryKey val articleTitle: String,
    val prevKey: Int?,
    val nextKey: Int?
)