package paiva.thiago.news.ui.viewModel

import androidx.paging.PagingData
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.mock
import paiva.thiago.news.data.model.Article
import paiva.thiago.news.data.repository.NewsRepository
import java.time.ZonedDateTime

@RunWith(JUnit4::class)
class NewsViewModelTest {

    private val newsRepository = mock<NewsRepository>()
    private lateinit var viewModel: NewsViewModel

    @Before
    fun setUp() {
        val mockPagingData = PagingData.from(listOf<Article>())
        Mockito.`when`(newsRepository.getPaginatedArticles()).thenReturn(flowOf(mockPagingData))
        viewModel = NewsViewModel(newsRepository)
    }

    @Test
    fun testGetArticlesFromRepository() {
        viewModel
        Mockito.verify(newsRepository).getPaginatedArticles()
    }

    @Test
    fun testOnHeadlineClicked_updatesSelectedArticle() = runTest {
        val article = Article(
            title = "Test Article",
            imageUrl = null,
            date = ZonedDateTime.now(),
            description = null,
            content = null
        )

        viewModel.onHeadlineClicked(article)

        Truth.assertThat(viewModel.selectedArticle).isEqualTo(article)
    }
}