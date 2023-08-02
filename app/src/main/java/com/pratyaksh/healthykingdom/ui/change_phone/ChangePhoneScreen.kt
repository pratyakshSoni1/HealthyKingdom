package com.pratyaksh.healthykingdom.ui.change_phone

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow

@Composable
fun ChangePhoneScreen(
    activity: Activity,
    getCurrentUser: Flow<Resource<String?>>,
    navController: NavHostController,
    setSuccessOtpVerification: (onVerify: () -> Unit) -> Unit,
    setResendToken: ( resendToken: PhoneAuthProvider.ForceResendingToken ) -> Unit
) {

    val viewModel: ChangePhoneVM = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = true, block = {
        viewModel.initScree(getCurrentUser)
    })

    Scaffold(
        topBar = {
            SimpleTopBar(onBackPress = { navController.popBackStack() }, title = "Change Password")
        }
    ) {
        Box(Modifier.padding(it), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp, 14.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                AppTextField(
                    value = uiState.newPhoneTxt,
                    onValueChange = { viewModel.updateOldPassTxt(it) },
                    hint = "New Phone Number",
                    keyboard = KeyboardType.Phone
                )
                Spacer(Modifier.height(6.dp))
                AppTextField(
                    value = uiState.oldPassTxt,
                    onValueChange = { viewModel.updateNewPassTxt(it) },
                    hint = "Password",
                    keyboard = KeyboardType.Password
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Forgot Password ?",
                    color = Color.Blue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.sendOtpSendUseCase(
                                phone= uiState.newPhoneTxt,
                                activity= activity,
                                onVerificationComplete = {
                                    navController.navigate(Routes.FORGOT_PASSWORD_SCREEN.route)
                                },
                                onVerificationFailed = {
                                    viewModel.toggleError(true, "Phone number can't be verified")
                                },
                                onCodeSent = { verId, resendToken ->
                                    setResendToken(resendToken)
                                    viewModel.updateVerificationId(verId)
                                    setSuccessOtpVerification{
                                        navController.navigate(Routes.FORGOT_PASSWORD_SCREEN.route)
                                    }
                                    navController.navigate(Routes.OTP_VERIFICATION_SCREEN.route+"/${uiState.newPhoneTxt}/${uiState.verificationId}")
                                }
                            )

                        },
                    textAlign = TextAlign.Center
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("cancel", modifier = Modifier.weight(1f), color = Color.Red)

                    Button(
                        onClick = {
                            viewModel.validateNewPhone()
                            if (viewModel.verifyPassword()) {
                                viewModel.sendOtpSendUseCase(
                                    phone = uiState.newPhoneTxt,
                                    activity = activity,
                                    onVerificationFailed = {
                                        viewModel.toggleError(
                                            true,
                                            "Phone number verification failed"
                                        )
                                    },
                                    onVerificationComplete = {
                                        viewModel.changePhone()
                                    },
                                    onCodeSent = { verId, resendToken ->
                                        viewModel.updateVerificationId(verId)
                                        setResendToken( resendToken )
                                        setSuccessOtpVerification{
                                            viewModel.changePhone()
                                        }
                                        navController.navigate(Routes.OTP_VERIFICATION_SCREEN.route+"/${uiState.newPhoneTxt}/$verId")
                                    }
                                )
                            } else {
                                viewModel.toggleError(true, "Wrong Password")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF0027FF)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 14.dp)
                    ) {
                        Text("Change Password", color = Color.White)
                    }
                }

            }
            if (uiState.isLoading) {
                LoadingComponent(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1f)
                )
            }

            if (uiState.isError) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = uiState.errorTxt, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }


}