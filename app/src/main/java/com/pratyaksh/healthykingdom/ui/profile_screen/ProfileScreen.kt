package com.pratyaksh.healthykingdom.ui.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.Scaffold
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
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.ErrorDialog
import com.pratyaksh.healthykingdom.ui.utils.GenderChooser
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.LocationChooserDialog
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import org.osmdroid.util.GeoPoint

@Composable
fun ProfileScreen(
    getCurrentUserId: Flow<Resource<String?>>,
    navController: NavHostController
) {
    val viewModel: ProfileScreenVM = hiltViewModel()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.initScreenState(getCurrentUserId)
    })

    LaunchedEffect(key1 = viewModel.uiState.isLoading, block = {
        if (viewModel.uiState.isLoading) {
            viewModel.toggleUpdateButton(false)
        } else {
            viewModel.toggleUpdateButton(true)
        }
    })

    Box(
        contentAlignment = Alignment.Center
    ) {

        Scaffold(
            topBar = {
                SimpleTopBar(
                    onBackPress = { navController.popBackStack() },
                    title = if (viewModel.uiState.isEditingMode) "Upate Profile" else "Profile",
                    {
                        Text(
                            if (viewModel.uiState.isEditingMode) "Update" else "Edit",
                            color = Color.Blue,
                            modifier = Modifier.clickable {
                                if (viewModel.uiState.isEditingMode)
                                    viewModel.updateDetails()
                                else
                                    viewModel.toggleEditingMode(true)
                            },
                        )
                    }
                )
            }
        ) {
            Column(
                Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(enabled = true, state = rememberScrollState())
                    .padding(vertical = 16.dp, horizontal = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    viewModel.uiState.name,
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
                Spacer(Modifier.height(12.dp))
            }
        }


        FloatingComponents(viewModel = viewModel)

    }

}

@Composable
private fun ColumnScope.PublicUserUi(viewModel: ProfileScreenVM) {
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
private fun ColumnScope.AmbulanceUserUi(viewModel: ProfileScreenVM) {
    if (viewModel.uiState.accountType == AccountTypes.AMBULANCE) {
        AppTextField(
            isEditable = viewModel.uiState.isEditingMode,
            value = viewModel.uiState.vehicleNumber, onValueChange = {
                viewModel.onVehicleNumberChange(it)
            },
            hint = "Vehicle Number"
        )
    }
}

@Composable
private fun ColumnScope.NonHospitalUserUi(viewModel: ProfileScreenVM) {
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
            isEditable = viewModel.uiState.isEditingMode,
            value = viewModel.uiState.age, onValueChange = {
                viewModel.onAgeChange(it)
            },
            hint = "Age",
            keyboard = KeyboardType.Number
        )
    }

}

@Composable
private fun ColumnScope.CommonUsersUi(viewModel: ProfileScreenVM) {
    NonHospitalUserUi(viewModel = viewModel)
    Spacer(Modifier.height(8.dp))

    AppTextField(
        isEditable = viewModel.uiState.isEditingMode,
        value = viewModel.uiState.name, onValueChange = {
            viewModel.onNameValueChange(it)
        },
        hint = if (viewModel.uiState.accountType == AccountTypes.HOSPITAL) "Hospital Name" else "Full Name"
    )
    Spacer(Modifier.height(8.dp))

    AppTextField(
        isEditable = viewModel.uiState.isEditingMode,
        value = viewModel.uiState.mail, onValueChange = {
            viewModel.onMailValueChange(it)
        },
        hint = "E-Mail"
    )
    Spacer(Modifier.height(8.dp))

}

@Composable
private fun ColumnScope.LocationComponent(viewModel: ProfileScreenVM) {
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
private fun BoxScope.FloatingComponents(viewModel: ProfileScreenVM) {
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
            onClose = { viewModel.toggleErrorDialog(false) }
        )
    }

    if (viewModel.uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x12000000))
        ) {
            LoadingComponent(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.6f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            )
        }
    }
}











