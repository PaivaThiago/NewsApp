package paiva.thiago.news.app

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import paiva.thiago.news.data.api.NewsApi
import paiva.thiago.news.data.database.NewsDatabase
import paiva.thiago.news.data.database.dao.ArticleDAO
import paiva.thiago.news.data.mediator.NewsRemoteMediator
import paiva.thiago.news.data.repository.NewsRepository
import paiva.thiago.news.utils.BASE_URL
import paiva.thiago.news.utils.DATABASE_NAME
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(db: NewsDatabase): ArticleDAO {
        return db.articleDao()
    }

    @Provides
    @Singleton
    fun provideNewsRemoteMediator(newsApi: NewsApi, newsDatabase: NewsDatabase): NewsRemoteMediator {
        return NewsRemoteMediator(newsApi, newsDatabase)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(dao: ArticleDAO, remoteMediator: NewsRemoteMediator): NewsRepository {
        return NewsRepository(dao, remoteMediator)
    }
}
