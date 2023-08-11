package com.pratyaksh.healthykingdom.ui.user_registration

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.ui.utils.AccountTypeChooser
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.ErrorDialog
import com.pratyaksh.healthykingdom.ui.utils.GenderChooser
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.LocationChooserDialog
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.LoginSignupStatus
import com.pratyaksh.healthykingdom.utils.Routes
import org.osmdroid.util.GeoPoint

@Composable
fun RegisterHospital(
    activity: Activity,
    viewModel: RegisterScreenVM = hiltViewModel(),
    navController: NavHostController,
    onResendTokenReceived: (PhoneAuthProvider.ForceResendingToken, Users) -> Unit
) {
    LaunchedEffect(key1 = Unit, block = {
        viewModel.initScreenState(
            RegistrationScreenUiState(
                isValidationStep = false
            )
        )
    })

    Box(
        contentAlignment = Alignment.Center
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(enabled = true, state = rememberScrollState())
                .padding(vertical = 16.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Sign Up",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            )
            Spacer(Modifier.height(28.dp))

            CommonUsersUi(viewModel = viewModel)
            Spacer(Modifier.height(8.dp))

            AmbulanceUserUi(viewModel = viewModel)
            Spacer(Modifier.height(8.dp))

            LocationComponent(viewModel = viewModel)

            PublicUserUi(viewModel = viewModel)
            Spacer(Modifier.height(32.dp))

            ActionButtons(
                onLogin = {
                    navController.navigate(route = Routes.LOGIN_SCREEN.route) {
                        launchSingleTop = true
                    }
                },
                onRegister = {
                    onRegisterUser(
                        viewModel, activity,
                        navController = navController,
                        onResendTokenReceived
                    )
                }
            )
            Spacer(Modifier.height(12.dp))
        }

        FloatingComponents(viewModel = viewModel)

    }

}

private fun onRegisterUser(
    viewModel: RegisterScreenVM, activity: Activity,
    navController: NavHostController,
    onResendTokenReceived: (PhoneAuthProvider.ForceResendingToken, Users) -> Unit
) {
    viewModel.apply {
        toggleLoadingScr(true)
        when(viewModel.verifySignUpDetails()){
            LoginSignupStatus.STATUS_SIGNUP_SUCCESS -> {
                otpSendUseCase(
                    viewModel.uiState.phone,
                    activity,
                    onVerificationComplete = { credential ->
                        navController.navigate(Routes.HOME_NAVGRAPH.route) {
                            popUpTo(Routes.SIGNUP_NAVGRAPH.route) { inclusive = true }
                        }
                    },
                    onVerificationFailed = { e ->
                        toggleErrorDialog(true, "Failed to verify, tray again later") {
                            viewModel.toggleErrorDialog(
                                false
                            )
                        }
                    },
                    onCodeSent = { verId, resendToken ->
                        saveResendTokenAndVerId(resendToken, verId)
                        onResendTokenReceived(
                            resendToken,
                            viewModel.getUser()
                        )
                        toggleLoadingScr(false)
                        navController.navigate(route = Routes.REG_OTP_VALIDATION_SCREEN.route + "/${viewModel.uiState.phone}/$verId")
                    }
                )
            }
            LoginSignupStatus.STATUS_INVALID_DETAILS -> toggleErrorDialog(true, "Invalid Details"){ toggleErrorDialog(false) }
            LoginSignupStatus.STATUS_SIGNUP_FAILED -> toggleErrorDialog(true, "Signup failed"){ toggleErrorDialog(false) }
            LoginSignupStatus.STATUS_SIGNUP_INVALID_NAME -> toggleErrorDialog(true, "Invalid Name, Please enter a valid name without any number or special character"){ toggleErrorDialog(false) }
            LoginSignupStatus.STATUS_SIGNUP_INVALID_NUMBER -> toggleErrorDialog(true, "Invalid Number, Enter a valid 10 digit number with country code"){ toggleErrorDialog(false) }
            LoginSignupStatus.STATUS_SIGNUP_PASSWORD_UNMATCHED -> toggleErrorDialog(true, "Password doesn't match with confirm password"){ toggleErrorDialog(false) }
            LoginSignupStatus.STATUS_SIGNUP_INVALID_PASSWORD_PATTERN -> toggleErrorDialog(true, "Password must contain a small, capital case alphabet,a number and a special character"){ toggleErrorDialog(false) }
            else -> Unit
        }

    }

}

@Composable
private fun ColumnScope.PublicUserUi(viewModel: RegisterScreenVM) {
    if (viewModel.uiState.accountType == AccountTypes.PUBLIC_USER) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(100.dp))
                .background(Color(0xFFE4E4E4))
                .padding(horizontal = 14.dp),
        ) {
            Text(
                "Provide Your Location",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))

            Switch(
                checked = viewModel.uiState.providesLocation,
                onCheckedChange = { viewModel.toggleProvideLoc(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Blue,
                    checkedTrackColor = Color(0x80007BFF)
                )
            )
        }

    }

}

@Composable
private fun ColumnScope.AmbulanceUserUi(viewModel: RegisterScreenVM) {
    if (viewModel.uiState.accountType == AccountTypes.AMBULANCE) {
        AppTextField(
            value = viewModel.uiState.vehicleNumber, onValueChange = {
                viewModel.onVehicleNumberChange(it)
            },
            hint = "Vehicle Number"
        )
    }
}

@Composable
private fun ColumnScope.NonHospitalUserUi(viewModel: RegisterScreenVM) {
    if (viewModel.uiState.accountType != AccountTypes.HOSPITAL) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            GenderChooser(
                onGenderChange = { viewModel.onGenderChange(it) },
                viewModel.uiState.gender
            )
        }
        Spacer(Modifier.height(8.dp))

        AppTextField(
            value = viewModel.uiState.age, onValueChange = {
                viewModel.onAgeChange(it)
            },
            hint = "Age",
            keyboard = KeyboardType.Number
        )
    }

}

@Composable
private fun ColumnScope.CommonUsersUi(viewModel: RegisterScreenVM) {
    AccountTypeChooser(
        isExpanded = viewModel.uiState.isAccMenuExpanded,
        accountType = viewModel.uiState.accountType,
        onToggleExpand = viewModel::toggleAccMenu,
        onAccChange = viewModel::onAccChange,
        onToggle = viewModel::toggleAccMenu
    )
    Spacer(Modifier.height(8.dp))

    NonHospitalUserUi(viewModel = viewModel)
    Spacer(Modifier.height(8.dp))

    AppTextField(
        value = viewModel.uiState.name, onValueChange = {
            viewModel.onNameValueChange(it)
        },
        hint = if (viewModel.uiState.accountType == AccountTypes.HOSPITAL) "Hospital Name" else "Full Name"
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

}

@Composable
private fun ColumnScope.LocationComponent(viewModel: RegisterScreenVM) {
    if (
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
                    .height(150.dp),
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
        Spacer(Modifier.height(22.dp))
    }

}

@Composable
private fun BoxScope.FloatingComponents(viewModel: RegisterScreenVM) {
    if (viewModel.uiState.showLocationChooser) {
        LocationChooserDialog(
            onSelectLocation = {
                viewModel.onLocationValueChange(it)
                viewModel.toggleLocationChooser(false)
            },
            onCancel = {
                viewModel.toggleLocationChooser(false)
            }
        )
    }

    if (viewModel.uiState.showError) {
        ErrorDialog(
            text = viewModel.uiState.errorText,
            onClose = { viewModel.uiState.onErrorCloseAction() }
        )
    }

    if (viewModel.uiState.isLoading) {
        LoadingComponent(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        )
    }
}

@Composable
private fun ActionButtons(
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    Button(
        onClick = {
            onRegister()
        },
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF0166FF),
        )
    ) {
        Text(
            text = "Register",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }

    Button(
        onClick = {
            onLogin()
        },
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
        ),
        elevation = ButtonDefaults.elevation(
            0.dp,
            0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        )
    ) {
        Text(
            text = "Login",
            color = Color(0xFF0166FF),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }

}










