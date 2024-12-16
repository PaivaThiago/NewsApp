package paiva.thiago.news.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import paiva.thiago.news.data.database.dao.ArticleDAO
import paiva.thiago.news.data.database.dao.RemoteKeysDao
import paiva.thiago.news.data.database.entity.ArticleEntity
import paiva.thiago.news.data.database.entity.RemoteKeys

@Database(entities = [ArticleEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDAO
    abstract fun remoteKeysDao(): RemoteKeysDao
}
