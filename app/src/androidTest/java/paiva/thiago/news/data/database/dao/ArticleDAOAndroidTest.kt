package paiva.thiago.news.data.database.dao

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import paiva.thiago.news.data.database.NewsDatabase
import paiva.thiago.news.data.database.entity.ArticleEntity

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ArticleDAOAndroidTest {

    private lateinit var database: NewsDatabase
    private lateinit var dao: ArticleDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>();
        database = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        dao = database.articleDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveArticles() = runBlocking {
        val article = ArticleEntity("Title", "url", "2024-12-18", "Desc", "Content")
        dao.insertAll(listOf(article))

        val retrieved = dao.pagingSource().load(PagingSource.LoadParams.Refresh(0, 1, false))
        assertNotNull(retrieved)
    }
}
