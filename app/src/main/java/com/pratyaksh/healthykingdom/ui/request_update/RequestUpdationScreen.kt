package com.pratyaksh.healthykingdom.ui.request_update

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.ui.request_update.components.SelectableGroupLabel
import com.pratyaksh.healthykingdom.ui.utils.FluidInfoDialog
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.ui.utils.LifeFluidTitle
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.toBloodGroupInfo
import com.pratyaksh.healthykingdom.utils.toPlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.toPlateletsGroupInfo
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RequestUpdationScreen(
    navController: NavHostController,
    hospitalId: String?
) {

    val viewModel = hiltViewModel<RequestUpdationVM>()
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(key1 = Unit, block = {
        viewModel.initViewModel(hospitalId)
    })

    Box(
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                SimpleTopBar(
                    onBackPress = { navController.popBackStack() },
                    title = "Request Pannel",
                    EndButtons = {
                        Text(
                            text = "Update",
                            color = if(uiState.isUpdationActive) Color.Blue else Color(0x230027FF),
                            modifier = Modifier.clickable {
                                if(uiState.isUpdationActive) {
                                    Log.d("ClickLogs", "Clicked update")
                                    viewModel.updateRequests()
                                }
                            }
                        )
                    }
                )
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
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFE4E4E4))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                IconButton(icon = Icons.Rounded.Info, onClick = { Unit }, size = 8.dp, backgroundColor = Color(0xFFE4E4E4))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    modifier= Modifier.weight(1f),
                                    text= "* Click to add/remove requests\n* Hold to view fluid info",
                                    color = Color.Black
                                )
                            }
                            Spacer(Modifier.height(6.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x4DFCC60E))
                                    .padding( start= 8.dp, end= 8.dp, bottom= 8.dp)
                            ) {
                                LifeFluidTitle(
                                    fluidType = LifeFluids.BLOOD,
                                    titlePrefix = "Requests"
                                )
                                FlowRow(
                                    maxItemsInEachRow = 4,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    for (group in BloodGroups.values()) {

                                        SelectableGroupLabel(
                                            fluidType = LifeFluids.BLOOD,
                                            group = group,
                                            isSelected = uiState.bloodRequest.find { it.type == group } != null,
                                            onShowGroupInfoDialog = {
                                                viewModel.showGroupInfo(
                                                    LifeFluids.BLOOD,
                                                    bloodGroupInfo = group.toBloodGroupInfo()
                                                )
                                            },
                                            toggleSelection = {
                                                viewModel.toggleBloodRequest(
                                                    group.toBloodGroupInfo()
                                                )
                                            }
                                        )

                                    }
                                }
                            }
                            Spacer(Modifier.height(6.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x3BDD0000))
                                    .padding( start= 8.dp, end= 8.dp, bottom= 8.dp)
                            ) {
                                LifeFluidTitle(
                                    fluidType = LifeFluids.PLASMA,
                                    titlePrefix = "Requests"
                                )
                                FlowRow(
                                    maxItemsInEachRow = 4,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    for (group in Plasma.values()) {
                                        SelectableGroupLabel(
                                            group = group,
                                            toggleSelection = { viewModel.togglePlasmaRequest(group.toPlasmaGroupInfo()) },
                                            onShowGroupInfoDialog = {
                                                viewModel.showGroupInfo(
                                                    LifeFluids.PLASMA,
                                                    plasmaGroupInfo = group.toPlasmaGroupInfo()
                                                )
                                            },
                                            isSelected = uiState.plasmaRequest.find { it.type == group } != null
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(6.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x33FF6060))
                                    .padding( start= 8.dp, end= 8.dp, bottom= 8.dp)
                            ) {
                                LifeFluidTitle(
                                    fluidType = LifeFluids.PLATELETS,
                                    titlePrefix = "Requests"
                                )
                                FlowRow(
                                    maxItemsInEachRow = 4,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    for (group in BloodGroups.values()) {
                                        SelectableGroupLabel(
                                            fluidType = LifeFluids.PLATELETS,
                                            group = group,
                                            isSelected = uiState.plateletsRequest.find { it.type == group } != null,
                                            onShowGroupInfoDialog = {
                                                viewModel.showGroupInfo(
                                                    LifeFluids.PLATELETS,
                                                    plateletsGroupInfo = group.toPlateletsGroupInfo()
                                                )
                                            },
                                            toggleSelection = {
                                                viewModel.togglePlateletsRequest(group.toPlateletsGroupInfo())
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(6.dp))


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
        )

        if (uiState.showGroupInfoDialog) {
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

        if (uiState.isLoading || uiState.hospitalId.isNullOrEmpty() ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingComponent(modifier = Modifier.size(80.dp))
            }
        }
    }

}