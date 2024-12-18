package paiva.thiago.news.ui.viewModel

import android.app.Application
import androidx.biometric.BiometricManager
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class BiometricViewModelTest {

    @Mock
    private lateinit var biometricManager: BiometricManager

    @Mock
    private lateinit var application: Application

    private lateinit var viewModel: BiometricViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = BiometricViewModel(application, biometricManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckBiometricSupport_supported() = runTest {
        Mockito.`when`(biometricManager.canAuthenticate(anyInt()))
            .thenReturn(BiometricManager.BIOMETRIC_SUCCESS)

        viewModel.checkBiometricSupport()

        Truth.assertThat(viewModel.biometricState.value).isEqualTo(
            BiometricViewModel.BiometricState.BiometricSupported
        )
        Mockito.verify(biometricManager).canAuthenticate(anyInt())
    }
}
