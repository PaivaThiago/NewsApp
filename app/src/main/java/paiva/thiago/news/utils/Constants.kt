package paiva.thiago.news.utils

// API
const val BASE_URL = "https://newsapi.org/"
const val ENDPOINT_TOP_HEADLINES = "/v2/top-headlines"
const val QUERY_PARAM_API_KEY = "apiKey"
const val QUERY_PARAM_SOURCES = "sources"
const val QUERY_PARAM_PAGE_SIZE = "pageSize"
const val QUERY_PARAM_PAGE = "page"

// Database
const val DATABASE_NAME = "news_db"
const val TABLE_ARTICLES = "articles"
const val TABLE_REMOTE_KEYS = "remote_keys"

// Date
const val DATE_PATTERN = "MMM d, yyyy HH:mm"

// Paging
const val PAGE_SIZE = 20
const val STARTING_PAGE_INDEX = 1
const val TIMEOUT = 5000L

// Logs
const val LOG_TAG_BIOMETRIC = "Biometric"
const val LOG_TAG_NEWS_REMOTE_MEDIATOR = "NewsRemoteMediator"

// Navigation
const val ROUTE_HEADLINES = "headlines"
const val ROUTE_DETAILS = "details"

// Coil
const val DISK_CACHE_DIR = "image_cache"