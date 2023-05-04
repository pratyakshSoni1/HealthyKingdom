package com.pratyaksh.healthykingdom.ui.hospital_registration

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.data.dto.toFBGeopoint
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.ui.utils.AccTypeMenuItem
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.ErrorDialog
import com.pratyaksh.healthykingdom.ui.utils.GenderChooser
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.LocationChooserDialog
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Routes
import org.osmdroid.util.GeoPoint

@Composable
fun RegisterHospital (
    activity: Activity,
    viewModel : RegisterHospitalVM = hiltViewModel(),
    navController:NavHostController,
    onResendTokenReceived: (PhoneAuthProvider.ForceResendingToken, Users) -> Unit
){
    LaunchedEffect(key1 = Unit, block = {
        viewModel.initScreenState(
            RegistrationScreenUiState(
                isValidationStep = false
            )
        )
    })

    Box(
        contentAlignment= Alignment.Center
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Register",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(22.dp))


            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 12.dp),
            ) {

                Text(
                    "Account Type: ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.width(4.dp))

                Box {
                    Row(
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.toggleAccMenu()
                            }
                    ) {
                        Icon(imageVector= Icons.Rounded.KeyboardArrowDown, contentDescription = "select account type")
                        AccTypeMenuItem(
                            name = viewModel.uiState.accountType.type,
                            painterResource(id = viewModel.uiState.accountType.img)
                        )
                    }

                    DropdownMenu(
                        expanded = viewModel.uiState.isAccMenuExpanded,
                        onDismissRequest = { viewModel.toggleAccMenu(false) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.onAccChange(AccountTypes.AMBULANCE)
                        }) {
                            AccTypeMenuItem(
                                name = "AMBULANCE",
                                img = painterResource(id = R.drawable.ambulance)
                            )
                        }

                        DropdownMenuItem(onClick = {
                            viewModel.onAccChange(AccountTypes.HOSPITAL)
                        }) {
                            AccTypeMenuItem(
                                name = "HOSPITAL",
                                img = painterResource(id = R.drawable.hospital)
                            )
                        }

                        DropdownMenuItem(onClick = {
                            viewModel.onAccChange(AccountTypes.HOSPITAL)
                        }) {
                            AccTypeMenuItem(
                                name = "Public User",
                                img = painterResource(id = R.drawable.ic_person)
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            AppTextField(
                value = viewModel.uiState.name, onValueChange = {
                    viewModel.onNameValueChange(it)
                },
                hint = if(viewModel.uiState.accountType == AccountTypes.HOSPITAL) "Name" else "Hospital Name"
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

            if(viewModel.uiState.accountType != AccountTypes.HOSPITAL){
                Row {
                    Text(
                        "Gender:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(Modifier.width(8.dp))

                    GenderChooser(
                        onGenderChange = { viewModel.onGenderChange(it) },
                        viewModel.uiState.gender
                    )
                }
                Spacer(Modifier.height(8.dp))

                AppTextField(
                    value = viewModel.uiState.name, onValueChange = {
                        viewModel.onAgeChange(it)
                    },
                    hint = "Age",
                    keyboard = KeyboardType.Number
                )
            }
            Spacer(Modifier.height(8.dp))

            if(viewModel.uiState.accountType == AccountTypes.AMBULANCE){
                AppTextField(
                    value = viewModel.uiState.name, onValueChange = {
                        viewModel.onVehicleNumberChange(it)
                    },
                    hint = "Vehicle Number"
                )
            }

            AppTextField(
                value = viewModel.uiState.password, onValueChange = {
                    viewModel.onPassValueChange(it)
                },
                hint = "Password",
                keyboard = KeyboardType.Password
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                value = viewModel.uiState.confirmPassword, onValueChange = {
                    viewModel.onConfirmPassValueChange(it)
                },
                hint = "Confirm password",
                keyboard = KeyboardType.Password
            )
            Spacer(Modifier.height(12.dp))

            if(viewModel.uiState.accountType == AccountTypes.PUBLIC_USER){
                Row {
                    Text(
                        "Provide Your Location:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(Modifier.width(8.dp))

                    Switch(
                        checked = viewModel.uiState.providesLocation,
                        onCheckedChange = { viewModel.toggleProvideLoc(it) }
                    )
                }

            }

            if(
                viewModel.uiState.accountType == AccountTypes.HOSPITAL ||
                (viewModel.uiState.providesLocation && viewModel.uiState.accountType == AccountTypes.PUBLIC_USER)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x80007BFF))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {

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
                            color = Color(0x80007BFF),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = {
                          onRegisterUser(
                              viewModel, activity,
                              navController = navController,
                              onResendTokenReceived
                          )
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
                modifier = Modifier
                    .fillMaxSize(0.45f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            )
        }
    }

}

private fun onRegisterUser(
    viewModel: RegisterHospitalVM, activity: Activity,
    navController: NavHostController,
    onResendTokenReceived: (PhoneAuthProvider.ForceResendingToken, Users) -> Unit
){
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
                    viewModel.getUser()
                )
                toggleLoadingScr(false)
                navController.navigate( route = Routes.OTP_VALIDATION_SCREEN.route+"/${viewModel.uiState.phone}/$verId" )
            }
        )
    }

}












