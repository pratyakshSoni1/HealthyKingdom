package com.pratyaksh.healthykingdom.ui.hospital_registration

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSignInUseCase
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.LocationChooserDialog
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import com.pratyaksh.healthykingdom.ui.utils.OtpTextField
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Composable
fun RegisterHospital (
    activity: Activity,
    viewModel : RegisterHospitalVM = hiltViewModel(),
    navController:NavHostController,
    onResendTokenReceived: (PhoneAuthProvider.ForceResendingToken) -> Unit
){
    LaunchedEffect(key1 = Unit, block = {
        viewModel.initScreenState(
            RegistrationScreenUiState(
                isValidationStep = false
            )
        )

    })

        Box {

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp, horizontal = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "Register Hospital",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(8.dp))

                    AppTextField(
                        value = viewModel.uiState.name, onValueChange = {
                            viewModel.onNameValueChange(it)
                        },
                        hint = "Name"
                    )
                    Spacer(Modifier.height(8.dp))

                    AppTextField(
                        value = viewModel.uiState.phone, onValueChange = viewModel::onPhoneValueChange,
                        hint = "+11225489547",
                        keyboard = KeyboardType.Number
                    )
                    Spacer(Modifier.height(8.dp))

                    AppTextField(
                        value = viewModel.uiState.mail, onValueChange = {
                            viewModel.onNameValueChange(it)
                        },
                        hint = "E-Mail"
                    )
                    Spacer(Modifier.height(8.dp))

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x80007BFF))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ){

                        MapLocationPreview(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.35f),
                            location = viewModel.uiState.location ?: GeoPoint(0.0, 0.0),
                            name = viewModel.uiState.name
                        )
                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.toggleLocationChooser(true)
                            },
                            shape = RoundedCornerShape(100.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF007BFF)
                            )
                        ) {
                            Text(
                                text = "Choose Location"
                            )
                        }

                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.apply {
                                otpSendUseCase(
                                    viewModel.uiState.phone,
                                    activity,
                                    onVerificationComplete = { credential ->

                                    },
                                    onVerificationFailed = { e ->

                                    },
                                    onCodeSent = { verId, resendToken ->
                                        saveResendTokenAndVerId(resendToken, verId)
                                        onResendTokenReceived(resendToken)
                                        navController.navigate( route = Routes.OTP_VALIDATION_SCREEN.route+"/${viewModel.uiState.phone}/$verId" )
                                    }
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "Register"
                        )
                    }

                }
                if(viewModel.uiState.showLocationChooser){
                    LocationChooserDialog(
                        onSelectLocation = {
                            viewModel.onLocationValueChange(it)
                            viewModel.toggleLocationChooser(false)
                        } ,
                        onCancel = {
                            viewModel.toggleLocationChooser(false)
                        }
                    )
                }
            }

}












