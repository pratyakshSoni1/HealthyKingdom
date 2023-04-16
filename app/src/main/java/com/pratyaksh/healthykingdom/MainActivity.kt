package com.pratyaksh.healthykingdom

import android.app.Instrumentation.ActivityResult
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.RegisterHospital
import com.pratyaksh.healthykingdom.ui.theme.HealthyKingdomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var necessaryPermissionGranted = true
    var permissionContract: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(applicationContext)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        permissionContract = registerForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
            permissions.forEach {
                if(!it.value){
                    necessaryPermissionGranted = false
                }
            }
        }

        setContent {
            HealthyKingdomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LaunchedEffect(key1 = Unit, block = {
                        askPermissions()
                    })

                    RegisterHospital(activity = this)
                }
            }
        }
    }

    private fun askPermissions(){

        permissionContract?.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )

    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthyKingdomTheme {
        Greeting("Android")
    }
}
