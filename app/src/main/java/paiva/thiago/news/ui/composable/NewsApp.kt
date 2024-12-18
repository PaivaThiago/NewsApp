package paiva.thiago.news.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import paiva.thiago.news.R
import paiva.thiago.news.data.model.Article
import paiva.thiago.news.ui.viewModel.NewsViewModel
import paiva.thiago.news.utils.ROUTE_DETAILS
import paiva.thiago.news.utils.ROUTE_HEADLINES

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp() {
    val newsViewModel: NewsViewModel = hiltViewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {
                    if (currentRoute == ROUTE_DETAILS) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    }
                },
                modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        MaterialTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(navController = navController, startDestination = ROUTE_HEADLINES) {
                    composable(ROUTE_HEADLINES) {
                        HeadlinesList(
                            headlines = newsViewModel.pagingData.collectAsLazyPagingItems(),
                            onHeadlineClick = { article ->
                                newsViewModel.onHeadlineClicked(article)
                                navController.navigate(ROUTE_DETAILS)
                            }
                        )
                    }

                    composable(ROUTE_DETAILS) {
                        val selectedArticle = newsViewModel.selectedArticle
                        if (selectedArticle != null) {
                            ArticleDetailsScreen(article = selectedArticle)
                        } else {
                            LaunchedEffect(Unit) {
                                navController.popBackStack()
                            }
                            ErrorScreen(message = stringResource(R.string.no_article_selected))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.error_message, message))
    }
}

@Composable
fun Loading() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun HeadlinesList(headlines: LazyPagingItems<Article>, onHeadlineClick: (Article) -> Unit) {
    LazyColumn {
        items(headlines.itemSnapshotList.items) { article ->
            article.let {
                ArticleItem(article = it, onClick = { onHeadlineClick(it) })
            }
        }

        when {
            headlines.loadState.refresh is LoadState.Error -> {
                item { ListError { headlines.retry() } }
            }
            headlines.loadState.append is LoadState.Error -> {
                item { ListError { headlines.retry() } }
            }
            headlines.loadState.refresh is LoadState.Loading -> {
                item { Loading() }
            }
            headlines.loadState.append is LoadState.Loading -> {
                item { Loading() }
            }
            headlines.loadState.prepend is LoadState.Loading -> {
                item { Loading() }
            }
        }
    }
}

@Composable
fun ListError(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onRetry() }) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun ArticleItem(article: Article, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            ArticleImage(article = article, modifier = Modifier.size(80.dp).padding(end = 16.dp))
            Column {
                Text(text = article.title, style = MaterialTheme.typography.titleMedium)
                Text(text = article.dateFormatted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun ArticleImage(article: Article, modifier: Modifier = Modifier) {
    val imageLoader: ImageLoader = LocalContext.current.imageLoader

    AsyncImage(
        model = article.imageUrl,
        contentDescription = null,
        placeholder = painterResource(R.drawable.ic_placeholder),
        error = painterResource(R.drawable.ic_broken_image),
        imageLoader = imageLoader,
        modifier = modifier
    )
}

@Composable
fun ArticleDetailsScreen(article: Article) {
    Column(modifier = Modifier.padding(16.dp)) {
        ArticleImage(article, modifier = Modifier.fillMaxWidth().size(250.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = article.title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = article.description ?: stringResource(R.string.no_description), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = article.content ?: stringResource(R.string.no_content), style = MaterialTheme.typography.bodySmall)
    }
}