package com.pratyaksh.healthykingdom.ui.otp_verification

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.ui.utils.ErrorDialog
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.OtpTextDisplay
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow

@Composable
fun OtpVerificationScreen(
    activity: Activity,
    onVerify: ()->Unit,
    phone:String,
    verificationId: String,
    resendToken: PhoneAuthProvider.ForceResendingToken,
    viewModel: OtpVerificationVM = hiltViewModel(),
    navController: NavHostController
){

    LaunchedEffect(Unit){
        viewModel.initScreen( verificationId, phone, resendToken )
    }
    Scaffold(
        topBar = {
            SimpleTopBar(
                onBackPress = { navController.popBackStack() },
                title = "Verify Phone"
            )
        }
    ) {

        Box(
            Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment= Alignment.Center) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Enter Verification code",
                    modifier=Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Text(
                    text = "sent to +${viewModel.uiState.phone}",
                    modifier=Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {

                    Box {
                        OtpTextDisplay(
                            text = viewModel.uiState.code
                        )

                        TextField(
                            value = viewModel.uiState.code,
                            onValueChange = { viewModel.onCodeChange(it) },
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f),
                        )
                    }

                }
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Resend",
                        color = if (viewModel.uiState.isResendAvail) Color.Blue else Color.LightGray,
                        modifier = Modifier.clickable(MutableInteractionSource(), indication = null) {
                            if (viewModel.uiState.isResendAvail) {
                                viewModel.otpSendUseCase(
                                    viewModel.uiState.phone,
                                    activity,
                                    resendToken = viewModel.uiState.resendToken!!,
                                    onVerificationComplete = {
                                        onVerify()
                                    },
                                    onVerificationFailed = {
                                        Log.d("VerificationLogs", "Can't verify otp${it.message}")
                                        viewModel.toggleErrorDialog(true, "Failed to verify")
                                    },
                                    onCodeSent = viewModel::updateVerificationIdAndToken
                                )
                            }
                        },
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(4.dp))

                    CircularProgressIndicator(
                        progress = viewModel.uiState.resendTimeout / 60f,
                        modifier = Modifier.size(26.dp),
                        strokeWidth = 4.dp,
                        color = Color.Blue
                    )

                }

                Box(
                    Modifier.weight(1f).fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = {
                            viewModel.otpSignInUseCase(
                                activity,
                                PhoneAuthProvider.getCredential(
                                    viewModel.uiState.verificationId,
                                    viewModel.uiState.code
                                ),
                                onVerifySuccess = {
                                    onVerify()
                                },
                                onVerificationFailed = {
                                    viewModel.toggleErrorDialog(
                                        true,
                                        "Verification failed, try later"
                                    )
                                },
                                onInvalidCoe = {
                                    viewModel.toggleErrorDialog(true, "Invalid Code")
                                }

                            )
                        },
                        modifier = Modifier.fillMaxWidth(0.75f),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF0166FF),
                        )
                    ) {
                        Text(
                            text = "Verify", color = Color.White
                        )
                    }
                }
            }

            if(viewModel.uiState.showError){
                ErrorDialog(
                    text = viewModel.uiState.errorText,
                    onClose= {
                        viewModel.toggleErrorDialog(true)
                    }
                )
            }

            if(viewModel.uiState.isLoading){
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0x32000000)), contentAlignment= Alignment.Center) {
                    LoadingComponent(
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        text = "Please Wait"
                    )
                }
            }

        }
    }
}
