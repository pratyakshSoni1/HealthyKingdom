package com.pratyaksh.healthykingdom.ui.settings

import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.domain.use_case.share_ambulance_loc.ShareAmbulanceLocSerice
import com.pratyaksh.healthykingdom.ui.fluid_update.NavMenuItem
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.ui.utils.VerifyPasswordDialog
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import com.pratyaksh.healthykingdom.utils.identifyUserTypeFromId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SettingsScreen(
    getCurrentUser: () -> Flow<Resource<String?>>,
    logoutUser: () -> Flow<Resource<Boolean>>,
    navController: NavHostController
) {

    val viewModel: SettingScreenVM = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val myCoroutine = rememberCoroutineScope()

    LaunchedEffect(key1 = true, block = {
        viewModel.initVM(getCurrentUser())
    })

    Box {
        if (uiState.userId != null) {
            Scaffold(
                topBar = {
                    SimpleTopBar(
                        onBackPress = { navController.popBackStack() },
                        title = "Settings"
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                Box(
                    modifier = Modifier.padding(it)
                ) {

                    Column {
                        if (identifyUserTypeFromId(uiState.userId)!!.equals(AccountTypes.AMBULANCE)) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Go Live", fontSize = 16.sp, modifier = Modifier.weight(1f))
                                Switch(
                                    checked = uiState.goLive,
                                    onCheckedChange = {
                                        viewModel.updateGoLive(it)
                                        myCoroutine.launch {
                                            if(it) {
                                                val liveLocServ = Intent( context.applicationContext, ShareAmbulanceLocSerice::class.java ).let {
                                                    it.putExtra("USER_ID", uiState.userId)
                                                }
                                                context.startForegroundService(liveLocServ)
                                            }else{
                                                context.stopService(Intent( context.applicationContext, ShareAmbulanceLocSerice::class.java ))
                                            }
                                        }

//                                        if (it)
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                                                context.startForegroundService(
//                                                    Intent(
//                                                        context.applicationContext,
//                                                        ShareAmbulanceLocSerice::class.java
//                                                    )
//                                                )
//                                            else
//                                                context.startService(
//                                                    Intent(
//                                                        context.applicationContext,
//                                                        ShareAmbulanceLocSerice::class.java
//                                                    )
//                                                )
//                                        else
//                                            context.stopService(
//                                                Intent(
//                                                    context,
//                                                    ShareAmbulanceLocSerice::class.java
//                                                )
//                                            )

                                    }
                                )
                            }
                        }

                        if (identifyUserTypeFromId(uiState.userId)!!.equals(AccountTypes.PUBLIC_USER)) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Show Current Location",
                                    fontSize = 16.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = uiState.showLiveLocPermit,
                                    onCheckedChange = {
                                        viewModel.updateShareLocPermit(it)
                                    }
                                )
                            }
                        }

                        NavMenuItem(
                            title = "Log out",
                            imageIcon = Icons.Rounded.ExitToApp,
                            onClick = {
                                if(viewModel.updateUserLogoutToFB()){
                                    logoutUser().onEach {
                                        when (it) {
                                            is Resource.Error -> viewModel.toggleError(true)
                                            is Resource.Loading -> viewModel.toggleLoading(true)
                                            is Resource.Success -> {
                                                navController.navigate(Routes.SIGNUP_NAVGRAPH.route) {
                                                    popUpTo(Routes.HOME_NAVGRAPH.route) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    Toast.makeText(context, "Unable to logout, try again later !", Toast.LENGTH_LONG).show()
                                }
                            })

                        NavMenuItem(
                            title = "Delete Account",
                            imageIcon = Icons.Rounded.ExitToApp,
                            onClick = {

                            })
                    }

                    //Should be at last so it's the first layer like a dialog
                    if (uiState.showVerifyPassDialog) {
                        VerifyPasswordDialog(
                            password = uiState.verifyDialogPassTxt,
                            onPassTxtChange = {
                                viewModel.updateDialogPassTxt(it)
                            },
                            onVerify = {
                                if (viewModel.verifyUser(uiState.verifyDialogPassTxt)) {
                                    Toast.makeText(
                                        context,
                                        "Deleting Account...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.deleteUser()
                                    navController.navigate(Routes.SIGNUP_NAVGRAPH.route) {
                                        popUpTo(Routes.HOME_NAVGRAPH.route) {
                                            inclusive = true
                                        }
                                    }
                                } else
                                    Toast.makeText(
                                        context,
                                        "Verification failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                viewModel.toggleVerifyDialog(false)
                            },
                            onClose = { viewModel.toggleVerifyDialog(false) }
                        )
                    }

                }

            }
        } else {
            LoadingComponent(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
            )
        }

        if (uiState.isLoading) {
            LoadingComponent(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
            )
        }

        if (uiState.isError) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                textAlign = TextAlign.Center,
                text = "Unexpected Error, please comeback later !"
            )
        }

    }

}