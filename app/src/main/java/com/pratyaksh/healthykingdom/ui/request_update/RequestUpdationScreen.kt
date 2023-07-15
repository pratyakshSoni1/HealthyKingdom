package com.pratyaksh.healthykingdom.ui.request_update

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.domain.model.getQuantityMap
import com.pratyaksh.healthykingdom.ui.utils.FluidInfoDialog
import com.pratyaksh.healthykingdom.ui.utils.GroupLabel
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.LifeFluids

@Composable
fun RequestUpdationScreen(
    navController: NavHostController,
    hospitalId: String
) {

    val viewModel = hiltViewModel<RequestUpdationVM>()
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchRequests()
    })

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
                    if (!uiState.isError) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(21.dp))
                                .background(Color(0xFFD2E2FA))
                                .padding(8.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background()
                            ) {
                                FlowRow(
                                    maxItemsInEachRow = 4,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {

                                    val availPlatelets = uiState.platelets.getQuantityMap()
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
                                }
                            }


                        }

                    } else if (uiState.isLoading) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingComponent(modifier = Modifier.size(80.dp))
                        }
                    } else {
                        Text(
                            "Unexpected Error\n Try again later.",
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    Spacer(Modifier.height(12.dp))

                }

            }
    }
    )

    if (uiState.showFluidDialog) {

        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x20000000))
        ) {
            FluidInfoDialog(
                fluidType = uiState.dialogFluidType!!,
                canDonateTo = if (uiState.dialogPlateletsGroup != null) uiState.dialogPlateletsGroup!!.canDonateTo
                else uiState.dialogBloodGroup?.canDonateTo ?: emptyList(),

                canReceivefrom = if (uiState.dialogPlateletsGroup != null) uiState.dialogPlateletsGroup!!.canReceiveFrom
                else uiState.dialogBloodGroup?.canReceiveFrom ?: emptyList(),

                onDismissReq = { viewModel.dismissFluidDialog() },
                canRecPlasmaFrom = uiState.dialogPlasmaGroup?.canReceiveFrom
                    ?: emptyList(),
                canDonPlasmaTo = uiState.dialogPlasmaGroup?.canDonateTo ?: emptyList(),
                bloodGroup = uiState.dialogBloodGroup,
                plasmaGroup = uiState.dialogPlasmaGroup,
                plateletsGroup = uiState.dialogPlateletsGroup
            )
        }

    }

}


}