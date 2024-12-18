package paiva.thiago.news.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import paiva.thiago.news.data.model.Article
import paiva.thiago.news.data.repository.NewsRepository
import paiva.thiago.news.utils.TIMEOUT
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    newsRepository: NewsRepository
) : ViewModel() {
    val pagingData: StateFlow<PagingData<Article>> = newsRepository.getPaginatedArticles()
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT),
            initialValue = PagingData.empty()
        )

    var selectedArticle: Article? = null
        private set

    fun onHeadlineClicked(article: Article) {
        selectedArticle = article
    }
}
