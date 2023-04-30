package com.pratyaksh.healthykingdom.ui.hospital_registration

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.data.dto.toFBGeopoint
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.ErrorDialog
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.LocationChooserDialog
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import com.pratyaksh.healthykingdom.utils.Routes
import org.osmdroid.util.GeoPoint

@Composable
fun RegisterHospital (
    activity: Activity,
    viewModel : RegisterHospitalVM = hiltViewModel(),
    navController:NavHostController,
    onResendTokenReceived: (PhoneAuthProvider.ForceResendingToken, HospitalsDto) -> Unit
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
            Spacer(Modifier.height(22.dp))

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
                    viewModel.onMailValueChange(it)
                },
                hint = "E-Mail"
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                value = viewModel.uiState.password, onValueChange = {
                    viewModel.onPassValueChange(it)
                },
                hint = "Password"
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                value = viewModel.uiState.confirmPassword, onValueChange = {
                    viewModel.onConfirmPassValueChange(it)
                },
                hint = "Confirm password"
            )
            Spacer(Modifier.height(12.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x80007BFF))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ){

                MapLocationPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
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
                        backgroundColor = Color.White
                    )
                ) {
                    Text(
                        text = "Choose Location",
                        color= Color(0x80007BFF),
                        modifier= Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            }

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = {
                    viewModel.apply {
                        toggleLoadingScr(true)
                        otpSendUseCase(
                            viewModel.uiState.phone,
                            activity,
                            onVerificationComplete = { credential ->
                                navController.navigate(Routes.HOME_NAVGRAPH.route){
                                    popUpTo(Routes.SIGNUP_NAVGRAPH.route){ inclusive = true }
                                }
                            },
                            onVerificationFailed = { e ->
                                toggleErrorDialog(true, "Failed to verify, tray again later")
                            },
                            onCodeSent = { verId, resendToken ->
                                saveResendTokenAndVerId(resendToken, verId)
                                onResendTokenReceived(
                                    resendToken,
                                    uiState.let {
                                        HospitalsDto(
                                            name= it.name,
                                            location = it.location?.toFBGeopoint(),
                                            id = it.name.replace(" ",""),
                                            mail = it.mail,
                                            phone= it.phone,
                                            availBloods = emptyList(),
                                            availPlasma = emptyList(),
                                            availPlatelets = emptyList(),
                                            password = it.password
                                        )
                                    }
                                )
                                toggleLoadingScr(false)
                                navController.navigate( route = Routes.OTP_VALIDATION_SCREEN.route+"/${viewModel.uiState.phone}/$verId" )
                            }
                        )
                    }
                },
                shape= RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF007BFF),
                )
            ) {
                Text(
                    text = "Register",
                    color= Color.White,
                    modifier= Modifier
                        .fillMaxWidth(0.85f)
                        .padding(vertical = 2.dp),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    navController.navigate( route = Routes.LOGIN_SCREEN.route ){
                        launchSingleTop = true
                    }
                },
                shape= RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFCECECE),
                )
            ) {
                Text(
                    text = "Login",
                    color= Color.Black,
                    modifier= Modifier
                        .fillMaxWidth(0.85f)
                        .padding(vertical = 2.dp),
                    textAlign = TextAlign.Center
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

        if(viewModel.uiState.showError){
            ErrorDialog(
                text = viewModel.uiState.errorText,
                onClose = { viewModel.toggleErrorDialog(false )}
            )
        }

        if(viewModel.uiState.isLoading){
            LoadingComponent(
                modifier = Modifier.fillMaxSize(0.45f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            )
        }
    }

}












