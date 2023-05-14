package com.pratyaksh.healthykingdom.ui.fluid_update

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.ui.utils.FluidQuantityUpdater
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last

@Composable
fun FluidsUpdationScreen(
    navController: NavHostController,
    fluidType: LifeFluids?,
    viewModel: FluidUpdateScreenVM = hiltViewModel(),
    getCurrentUser:() -> Flow<Resource<String?>>
) {

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = true, block = {
        viewModel.toggleLoading()
        getCurrentUser().collectLatest {
                if (it is Resource.Success) viewModel.initScreen(it.data!!, fluidType!!)
                else viewModel.toggleError(true, "Error getting user info")
        }
    })

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    onBackPress = { navController.popBackStack() },
                    title = "Update ${fluidType?.name}"
                ){
                    Text(
                        text= "Update",
                        color= Color.Blue,
                        modifier=Modifier.clickable {
                            Log.d("ClickLogs", "Clicked update")
                            viewModel.onUpdateFluidToFireStore()
                        }
                    )
                }
            }
        ) { it ->
            Column(Modifier.padding(it), horizontalAlignment = Alignment.CenterHorizontally) {

                LazyVerticalGrid(columns = GridCells.Fixed(2),modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround, content = {

                    if (uiState.fluidType == LifeFluids.BLOOD || uiState.fluidType == LifeFluids.PLATELETS) {
                        items(BloodGroups.values()) { group ->
                            FluidQuantityUpdater(
                                type = uiState.fluidType!!,
                                group = group,
                                qty = when (group) {
                                    BloodGroups.A_POSITIVE -> uiState.availBloodGroups.aPos
                                    BloodGroups.A_NEGATIVE -> uiState.availBloodGroups.aNeg
                                    BloodGroups.B_POSITIVE -> uiState.availBloodGroups.bPos
                                    BloodGroups.B_NEGATIVE -> uiState.availBloodGroups.bNeg
                                    BloodGroups.O_POSITIVE -> uiState.availBloodGroups.oPos
                                    BloodGroups.O_NEGATIVE -> uiState.availBloodGroups.oNeg
                                    BloodGroups.AB_POSITIVE -> uiState.availBloodGroups.abPos
                                    BloodGroups.AB_NEGATIVE -> uiState.availBloodGroups.abNeg
                                },
                                onIncQty = { viewModel.incBloodGroupQty(group) },
                                onDecQty = { viewModel.decBloodGroupQty(group) }
                            )
                        }
                    } else if (uiState.fluidType == LifeFluids.PLASMA) {
                        items(Plasma.values()) {group ->
                            FluidQuantityUpdater(
                                group = group,
                                qty = when (group) {
                                    Plasma.PLASMA_A -> uiState.availPlasma.aGroup
                                    Plasma.PLASMA_B -> uiState.availPlasma.bGroup
                                    Plasma.PLASMA_AB -> uiState.availPlasma.abGroup
                                    Plasma.PLASMA_O -> uiState.availPlasma.oGroup
                                                },
                                onIncQty = { viewModel.incPlasmaGroupQty(group) },
                                onDecQty = { viewModel.decPlasmaGroupQty(group) }
                            )
                        }
                    }
                })

            }
        }

        if(uiState.isLoading){
            LoadingComponent(modifier = Modifier.size(180.dp))
        }

    }
}