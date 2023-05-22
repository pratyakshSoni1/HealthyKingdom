package com.pratyaksh.healthykingdom

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.pratyaksh.healthykingdom.ui.theme.HealthyKingdomTheme
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var necessaryPermissionGranted = true
    var permissionContract: ActivityResultLauncher<Array<String>>? = null

    val Context.userLoggedDS: DataStore<Preferences> by preferencesDataStore(Constants.USER_LOGGED_DS)
    var startRoute: Routes = Routes.SIGNUP_NAVGRAPH


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
                    var isLoading by remember{ mutableStateOf(true) }

                    LaunchedEffect(key1 = Unit, block = {
                        askPermissions()
                        readLoggedUser().collectLatest {
                            if( it is Resource.Success  ){
                                startRoute = if(!it.data.isNullOrBlank()){ Routes.HOME_NAVGRAPH }else { Routes.SIGNUP_NAVGRAPH }
                                delay(1000L)
                                isLoading = false
                            }else {
                                startRoute = Routes.SIGNUP_NAVGRAPH
                                isLoading = false
                            }
                        }

                    })

                    if(isLoading) {
                        LoadingComponent(modifier = Modifier.fillMaxSize())
                    }else{
                        Navigation(
                            startDestination = startRoute,
                            activity = this,
                            getCurrentLoggedUser = { readLoggedUser() },
                            updateCurrentLoggedUser = { updateLoggedUser(it) }
                        )
                    }
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

    private fun updateLoggedUser(newUserId: String?) = flow<Resource<Boolean>>{
            emit(Resource.Loading( "Updating current logged user"))
            try {
                val userLoggedPrefKey = stringPreferencesKey(Constants.USER_LOGGED_KEY)
                baseContext.userLoggedDS.edit { userDS ->
                    userDS[userLoggedPrefKey] = newUserId ?: ""
                }
                emit(Resource.Success(true))
                Log.d("DataStorePrefLogs", "Updated to: $newUserId")
            }catch(e: Exception){
                e.printStackTrace()
                emit(Resource.Error(e.message))
                Log.d("DataStorePrefLogs", "Failed Updated")
            }

    }

    private fun readLoggedUser() = flow<Resource<String?>>{
        try {
            val userLoggedPreKey = stringPreferencesKey(Constants.USER_LOGGED_KEY)
            val user = baseContext.userLoggedDS.data.first()[userLoggedPreKey]
            emit(
                Resource.Success(
                    if(user.isNullOrEmpty()) null else user
                )
            )
            Log.d("DataStorePrefLogs", "Current usr: $user")
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error(e.message))
            Log.d("DataStorePrefLogs", "Falied to read Current usr")
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
