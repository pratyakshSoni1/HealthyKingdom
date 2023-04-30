package com.pratyaksh.healthykingdom

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.pratyaksh.healthykingdom.ui.Navigation
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.RegisterHospital
import com.pratyaksh.healthykingdom.ui.theme.HealthyKingdomTheme
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var necessaryPermissionGranted = true
    var permissionContract: ActivityResultLauncher<Array<String>>? = null

    val Context.userLoggedDS: DataStore<Preferences> by preferencesDataStore(Constants.USER_LOGGED_DS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(applicationContext)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        permissionContract = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
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

                    Navigation(
                        startDestination = Routes.SIGNUP_NAVGRAPH,
                        activity = this,
                        getCurrentLoggedUser = { readLoggedUser() },
                        updateCurrentLoggedUser = { updateLoggedUser(it) }
                    )
                }
            }
        }
    }

    private fun askPermissions(){
        Log.d("Hello","I'm a log")
        permissionContract?.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    private fun updateLoggedUser(newUserId: String) = flow<Resource<Boolean>>{
        CoroutineScope(Dispatchers.IO).launch {
            emit(Resource.Loading( "Updating current logged user"))
            try {
                val userLoggedPrefKey = stringPreferencesKey(Constants.USER_LOGGED_KEY)
                baseContext.userLoggedDS.edit { userDS ->
                    userDS[userLoggedPrefKey] = newUserId
                }
                emit(Resource.Success(true))
            }catch(e: Exception){
                e.printStackTrace()
                emit(Resource.Error(e.message))
            }

        }
    }

    private fun readLoggedUser() = flow<Resource<String?>>{
        try {
            emit(Resource.Loading( "Getting current login info"))
            val userLoggedPreKey = stringPreferencesKey(Constants.USER_LOGGED_KEY)
            emit(Resource.Success(baseContext.userLoggedDS.data.first()[userLoggedPreKey]))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error(e.message))
        }
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
