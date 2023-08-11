package com.pratyaksh.healthykingdom

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import com.pratyaksh.healthykingdom.utils.network_connectivity.ConnectivityObserver
import com.pratyaksh.healthykingdom.utils.network_connectivity.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


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
                if (!it.value) {
                    necessaryPermissionGranted = false
                }
            }
        }

        val connectivityObserver = NetworkConnectivityObserver(applicationContext)

        setContent {
            HealthyKingdomTheme {
                // A surface container using the 'background' color from the theme
                val networkStatus = connectivityObserver.observer()
                    .collectAsState(initial = ConnectivityObserver.Status.Unavailable)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var isLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(key1 = Unit, block = {
                        askPermissions()
                        readLoggedUser().collectLatest {
                            if (it is Resource.Success) {
                                startRoute = if (!it.data.isNullOrBlank()) {
                                    Routes.HOME_NAVGRAPH
                                } else {
                                    Routes.SIGNUP_NAVGRAPH
                                }
                                delay(1000L)
                                isLoading = false
                            } else {
                                startRoute = Routes.SIGNUP_NAVGRAPH
                                isLoading = false
                            }
                        }

                    })
                    Box(Modifier.fillMaxSize()) {
                        if (isLoading) {
                            LoadingComponent(modifier = Modifier.fillMaxSize())
                        } else {
                            Navigation(
                                startDestination = startRoute,
                                activity = this@MainActivity,
                                getCurrentLoggedUser = { readLoggedUser() },
                                updateCurrentLoggedUser = { updateLoggedUser(it) }
                            )
                        }

                        if (networkStatus.value == ConnectivityObserver.Status.Unavailable || networkStatus.value == ConnectivityObserver.Status.Unavailable) {
                            Dialog(onDismissRequest = { Unit }) {

                                Box(
                                    Modifier.fillMaxWidth(0.9f)
                                        .fillMaxHeight(0.55f).clip(RoundedCornerShape(14.dp))
                                        .background(Color.White)
                                        .padding(14.dp, 12.dp),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    IconButton(
                                        icon = Icons.Rounded.Close,
                                        onClick = { this@MainActivity.finish() }
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_network),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(0.35f),
                                            tint = Color.Red
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            "No Internet connection !",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun askPermissions() {
        Log.d("Hello", "I'm a log")
        permissionContract?.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    private fun updateLoggedUser(newUserId: String?) = flow<Resource<Boolean>> {
        emit(Resource.Loading("Updating current logged user"))
        try {
            val userLoggedPrefKey = stringPreferencesKey(Constants.USER_LOGGED_KEY)
            baseContext.userLoggedDS.edit { userDS ->
                userDS[userLoggedPrefKey] = newUserId ?: ""
            }
            emit(Resource.Success(true))
            Log.d("DataStorePrefLogs", "Updated to: $newUserId")
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message))
            Log.d("DataStorePrefLogs", "Failed Updated")
        }

    }

    private fun readLoggedUser() = flow<Resource<String?>> {
        try {
            val userLoggedPreKey = stringPreferencesKey(Constants.USER_LOGGED_KEY)
            val user = baseContext.userLoggedDS.data.first()[userLoggedPreKey]
            emit(
                Resource.Success(
                    if (user.isNullOrEmpty()) null else user
                )
            )
            Log.d("DataStorePrefLogs", "Current usr: $user")
        } catch (e: Exception) {
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
