package paiva.thiago.news.app

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import paiva.thiago.news.utils.BASE_URL

@RunWith(MockitoJUnitRunner::class)
class AppModuleTest {

    @Test
    fun `verify logging interceptor provides correct level`() {
        val interceptor = AppModule.provideLoggingInterceptor()
        assertEquals(HttpLoggingInterceptor.Level.BODY, interceptor.level)
    }

    @Test
    fun `verify OkHttpClient is correctly built`() {
        val loggingInterceptor = AppModule.provideLoggingInterceptor()
        val client = AppModule.provideOkHttpClient(loggingInterceptor)
        assertNotNull(client)
        assertTrue(client.interceptors.contains(loggingInterceptor))
    }

    @Test
    fun `verify Retrofit is correctly built`() {
        val loggingInterceptor = AppModule.provideLoggingInterceptor()
        val client = AppModule.provideOkHttpClient(loggingInterceptor)
        val retrofit = AppModule.provideRetrofit(client)
        assertNotNull(retrofit)
        assertEquals(BASE_URL, retrofit.baseUrl().toString())
    }
}