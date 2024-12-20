package paiva.thiago.news.ui.viewModel

import android.app.Application
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import paiva.thiago.news.R
import paiva.thiago.news.utils.LOG_TAG_BIOMETRIC
import javax.inject.Inject

@HiltViewModel
class BiometricViewModel @Inject constructor(
    private val application: Application,
    private val biometricManager: BiometricManager
) : AndroidViewModel(application) {

    private val _biometricState = MutableStateFlow<BiometricState>(BiometricState.CheckingSupport)
    val biometricState: StateFlow<BiometricState> = _biometricState

    private lateinit var biometricPrompt: BiometricPrompt

    sealed class BiometricState {
        data object CheckingSupport : BiometricState()
        data object BiometricSupported : BiometricState()
        data object BiometricNotSupported : BiometricState()
        data object BiometricNotEnrolled : BiometricState()
        data object AuthenticationStarted : BiometricState()
        data object AuthenticationSuccess : BiometricState()
        data class AuthenticationError(val message: String) : BiometricState()
        data class BiometricError(val message: String) : BiometricState()
    }

    init {
        checkBiometricSupport()
    }

    fun checkBiometricSupport() {
        viewModelScope.launch {
            val result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            when (result) {
                BiometricManager.BIOMETRIC_SUCCESS -> _biometricState.value = BiometricState.BiometricSupported
                else -> handleBiometricError(result)
            }
        }
    }

    private fun handleBiometricError(errorCode: Int) {
        val message = when (errorCode) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> application.getString(R.string.biometric_error_hardware_not_found)
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> application.getString(R.string.biometric_error_hardware_not_available)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> application.getString(R.string.biometric_error_no_fingerprint_enrolled)
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> application.getString(R.string.biometric_error_update_required)
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> application.getString(R.string.biometric_error_not_supported)
            else -> application.getString(R.string.biometric_error_unknown)
        }
        _biometricState.value = BiometricState.BiometricError(message)
        Log.d(LOG_TAG_BIOMETRIC, message)
    }

    fun authenticateUser(activity: FragmentActivity) {
        if (_biometricState.value != BiometricState.BiometricSupported) return

        _biometricState.value = BiometricState.AuthenticationStarted

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(application.getText(R.string.biometric_title))
            .setSubtitle(application.getText(R.string.biometric_subtitle))
            .setNegativeButtonText(application.getText(R.string.cancel))
            .build()

        val executor = ContextCompat.getMainExecutor(getApplication())

        biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    _biometricState.value = BiometricState.AuthenticationError("${application.getString(R.string.biometric_error_authentication)}: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    _biometricState.value = BiometricState.AuthenticationSuccess
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    _biometricState.value = BiometricState.AuthenticationError(application.getString(R.string.biometric_error_authentication))
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}