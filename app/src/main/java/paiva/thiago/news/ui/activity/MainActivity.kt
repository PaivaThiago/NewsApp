package paiva.thiago.news.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import paiva.thiago.news.ui.composable.NewsApp
import paiva.thiago.news.ui.theme.NewsTheme
import paiva.thiago.news.ui.viewModel.BiometricViewModel

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val biometricViewModel: BiometricViewModel = viewModel()
            NewsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (biometricViewModel.biometricState.collectAsState().value) {
                        BiometricViewModel.BiometricState.BiometricSupported -> {
                            biometricViewModel.authenticateUser(this@MainActivity)
                        }
                        BiometricViewModel.BiometricState.AuthenticationSuccess -> {
                            NewsApp()
                        }
                        BiometricViewModel.BiometricState.BiometricNotSupported,
                        BiometricViewModel.BiometricState.BiometricNotEnrolled -> {
                            NewsApp()
                        }
                        is BiometricViewModel.BiometricState.AuthenticationError -> {
                            Toast.makeText(
                                this@MainActivity,
                                (biometricViewModel.biometricState.collectAsState().value as BiometricViewModel.BiometricState.AuthenticationError).message,
                                Toast.LENGTH_SHORT
                            ).show()
                            NewsApp()
                        }
                        is BiometricViewModel.BiometricState.BiometricError -> {
                            Toast.makeText(
                                this@MainActivity,
                                (biometricViewModel.biometricState.collectAsState().value as BiometricViewModel.BiometricState.BiometricError).message,
                                Toast.LENGTH_SHORT
                            ).show()
                            NewsApp()
                        }
                        BiometricViewModel.BiometricState.AuthenticationStarted,
                        BiometricViewModel.BiometricState.CheckingSupport -> {
                            // Do nothing
                        }
                    }
                }
            }
        }
    }
}