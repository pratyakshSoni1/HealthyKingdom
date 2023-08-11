package com.pratyaksh.healthykingdom.ui.hospital_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.domain.model.getQuantityMap
import com.pratyaksh.healthykingdom.ui.utils.ErrorDialog
import com.pratyaksh.healthykingdom.ui.utils.FluidInfoDialog
import com.pratyaksh.healthykingdom.ui.utils.GroupLabel
import com.pratyaksh.healthykingdom.ui.utils.LifeFluidTitle
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import com.pratyaksh.healthykingdom.ui.utils.RequestsDisplayComponent
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.LifeFluids

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HospitalDetailsScreen(
    navController: NavHostController,
    hospitalId: String,
    viewModel: HospitalDetailsVM = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchHospital(hospitalId)
    })

    val uiState = viewModel.uiState

    Box(
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                SimpleTopBar(onBackPress = { navController.popBackStack() }, title = "Details")
            },
            content = { paddingValues ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp, vertical = 14.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (uiState.value.isError) {
                        ErrorDialog(text = uiState.value.errorTxt) {
                            navController.popBackStack()
                        }
                    } else if (uiState.value.hospital != null) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(21.dp))
                                .background(Color(0xFFD2E2FA))
                                .padding(8.dp)
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.hospital),
                                    contentDescription = null,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(Modifier.width(8.dp))

                                Text(
                                    text = uiState.value.hospital!!.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    modifier = Modifier.size(21.dp)
                                )
                                Spacer(Modifier.width(8.dp))

                                Text(
                                    text = uiState.value.hospital!!.phone,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(Modifier.height(2.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    modifier = Modifier.size(21.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = uiState.value.hospital!!.mail,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            MapLocationPreview(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                location = uiState.value.hospital!!.location,
                                name = uiState.value.hospital!!.name
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))

                        RequestsDisplayComponent(
                            requests = uiState.value.requests,
                            onBloodGroupClick = {
                                viewModel.showFluidInfoDialog(
                                    fluidType = LifeFluids.BLOOD,
                                    fluidBloodGroup = it,
                                )
                            },
                            onPlateletsGroupClick = {
                                viewModel.showFluidInfoDialog(
                                    fluidType = LifeFluids.PLATELETS,
                                    fluidPlateletsGroup = it,
                                )
                            },
                            onPlasmaGroupClick = {
                                viewModel.showFluidInfoDialog(
                                    fluidType = LifeFluids.PLASMA,
                                    fluidPlasmaGroup = it
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(22.dp))

                        Text(
                            text = "Available Fluids",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(21.dp))
                                .background(Color(0x33FF6060))
                                .padding(8.dp)
                        ) {
                            LifeFluidTitle(fluidType = LifeFluids.BLOOD, titlePrefix = "Available")

                            FlowRow(
                                maxItemsInEachRow = 4,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val availBlood = uiState.value.bloods.getQuantityMap()
                                if (availBlood.values.any { qty -> qty > 0 }) {
                                    for (group in availBlood) {
                                        if (group.value > 0) {
                                            Box(
                                                Modifier.clickable {
                                                    viewModel.showFluidInfoDialog(
                                                        LifeFluids.BLOOD,
                                                        fluidBloodGroup = group.key
                                                    )
                                                }
                                            ) {
                                                GroupLabel(
                                                    type = LifeFluids.BLOOD,
                                                    group = group.key.type,
                                                    qty = group.value,
                                                    fontSize = 24.sp
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    FluidNotAvailableTag()
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                            }


                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(21.dp))
                                .background(Color(0x4DFCC60E))
                                .padding(8.dp)
                        ) {

                            LifeFluidTitle(fluidType = LifeFluids.PLASMA, titlePrefix = "Available")

                            FlowRow(
                                maxItemsInEachRow = 4,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {

                                val availPlasma = uiState.value.plasma.getQuantityMap()
                                if (availPlasma.values.any { qty -> qty > 0 }) {
                                    for (group in availPlasma) {
                                        if (group.value > 0) {
                                            Box(
                                                Modifier.clickable {
                                                    viewModel.showFluidInfoDialog(
                                                        LifeFluids.PLASMA,
                                                        fluidPlasmaGroup = group.key
                                                    )
                                                }
                                            ) {
                                                GroupLabel(
                                                    qty = group.value,
                                                    fontSize = 24.sp,
                                                    plasma = group.key.type
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    FluidNotAvailableTag()
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(21.dp))
                                .background(Color(0x3BDD0000))
                                .padding(8.dp)
                        ) {

                            LifeFluidTitle(
                                fluidType = LifeFluids.PLATELETS,
                                titlePrefix = "Available"
                            )

                            FlowRow(
                                maxItemsInEachRow = 4,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {

                                val availPlatelets = uiState.value.platelets.getQuantityMap()
                                if (availPlatelets.values.any { qty -> qty > 0 }) {
                                    for (group in availPlatelets) {
                                        if (group.value > 0) {
                                            Box(
                                                Modifier.clickable {
                                                    viewModel.showFluidInfoDialog(
                                                        LifeFluids.BLOOD,
                                                        fluidPlateletsGroup = group.key
                                                    )
                                                }
                                            ) {
                                                GroupLabel(
                                                    type = LifeFluids.BLOOD,
                                                    group = group.key.type,
                                                    qty = group.value,
                                                    fontSize = 24.sp
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    FluidNotAvailableTag()
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                            }

                        }
                        Spacer(Modifier.height(12.dp))

                    }

                }
            }
        )

        if (uiState.value.showFluidDialog) {

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x20000000))
            ) {
                FluidInfoDialog(
                    fluidType = uiState.value.dialogFluidType!!,
                    canDonateTo = if (uiState.value.dialogPlateletsGroup != null) uiState.value.dialogPlateletsGroup!!.canDonateTo
                    else uiState.value.dialogBloodGroup?.canDonateTo ?: emptyList(),

                    canReceivefrom = if (uiState.value.dialogPlateletsGroup != null) uiState.value.dialogPlateletsGroup!!.canReceiveFrom
                    else uiState.value.dialogBloodGroup?.canReceiveFrom ?: emptyList(),

                    onDismissReq = { viewModel.dismissFluidDialog() },
                    canRecPlasmaFrom = uiState.value.dialogPlasmaGroup?.canReceiveFrom
                        ?: emptyList(),
                    canDonPlasmaTo = uiState.value.dialogPlasmaGroup?.canDonateTo ?: emptyList(),
                    bloodGroup = uiState.value.dialogBloodGroup,
                    plasmaGroup = uiState.value.dialogPlasmaGroup,
                    plateletsGroup = uiState.value.dialogPlateletsGroup
                )
            }

        }

        if (uiState.value.isLoading) {
            LoadingComponent(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            )

        }

    }

}

@Composable
private fun FluidNotAvailableTag() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_visibility),
            contentDescription = "null",
            tint = Color.Red,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            textAlign = TextAlign.Center,
            text = "Fluid not available",
            color = Color(0x3E000000),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

}

