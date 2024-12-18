package paiva.thiago.news.data.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import paiva.thiago.news.data.database.entity.RemoteKeys
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import paiva.thiago.news.data.database.NewsDatabase

@RunWith(AndroidJUnit4::class)
class RemoteKeysDaoAndroidTest {

    private lateinit var database: NewsDatabase
    private lateinit var remoteKeysDao: RemoteKeysDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).build()
        remoteKeysDao = database.remoteKeysDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAll_and_get_remote_keys() = runBlocking {
        val remoteKeys = listOf(
            RemoteKeys("article1", prevKey = 0, nextKey = 2),
            RemoteKeys("article2", prevKey = 1, nextKey = 3),
            RemoteKeys("article3", prevKey = 2, nextKey = 4)
        )
        remoteKeysDao.insertAll(remoteKeys)

        val remoteKey1 = remoteKeysDao.remoteKeysByArticleTitle("article1")
        val remoteKey2 = remoteKeysDao.remoteKeysByArticleTitle("article2")
        val remoteKey3 = remoteKeysDao.remoteKeysByArticleTitle("article3")

        assertEquals(remoteKeys[0], remoteKey1)
        assertEquals(remoteKeys[1], remoteKey2)
        assertEquals(remoteKeys[2], remoteKey3)
    }

    @Test
    fun clear_remote_keys() = runBlocking {
        val remoteKeys = listOf(
            RemoteKeys("article1", prevKey = 0, nextKey = 2),
            RemoteKeys("article2", prevKey = 1, nextKey = 3),
            RemoteKeys("article3", prevKey = 2, nextKey = 4)
        )
        remoteKeysDao.insertAll(remoteKeys)
        remoteKeysDao.clearRemoteKeys()

        val remoteKey1 = remoteKeysDao.remoteKeysByArticleTitle("article1")
        val remoteKey2 = remoteKeysDao.remoteKeysByArticleTitle("article2")
        val remoteKey3 = remoteKeysDao.remoteKeysByArticleTitle("article3")

        assertNull(remoteKey1)
        assertNull(remoteKey2)
        assertNull(remoteKey3)
    }
}