package com.pratyaksh.healthykingdom.ui.hospital_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.data.dto.toMapsGeopoint
import com.pratyaksh.healthykingdom.domain.model.getQuantityMap
import com.pratyaksh.healthykingdom.ui.utils.GroupLabel
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

@Composable
fun HospitalDetailsScreen(
    navController: NavHostController,
    hospitalId: String,
    viewModel: HospitalDetailsVM = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchHospital(hospitalId)
    })

    val uiState = viewModel.uiState.value

    Box(
        contentAlignment = Alignment.Center
    ){
        Scaffold(
            modifier= Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            topBar = {
                SimpleTopBar(onBackPress = { navController.popBackStack() }, title = "Details")
            },
            content = { it ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ){
                    if(uiState.hospital != null) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment =Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.hospital),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = uiState.hospital.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color= Color.Black
                            )
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                colorFilter = ColorFilter.tint(Color.Blue)
                            )
                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = uiState.hospital.phone,
                                fontSize = 16.sp,
                                color= Color.Black
                            )
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                colorFilter = ColorFilter.tint(Color.Blue)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = uiState.hospital.mail,
                                fontSize = 16.sp,
                                color= Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        MapLocationPreview(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            location = uiState.hospital.location,
                            name = uiState.hospital.name
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text= "Available Fluids", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row{
                            Image(
                                painter= painterResource(id =R.drawable.ic_blood),
                                modifier= Modifier.size(33.dp),
                                contentDescription = null,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text= "Available Bloods"
                            )
                        }
                        Spacer(Modifier.height(8.dp))

                        LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                            val availBlood = uiState.bloods.getQuantityMap()
                            items(availBlood.keys.toList()){group ->
                                if(availBlood[group]!! > 0 ){
                                    Box(
                                        Modifier.clickable {
                                            viewModel.showFluidInfoDialog( LifeFluids.BLOOD, group )
                                        }
                                    ){
                                        GroupLabel(type = LifeFluids.BLOOD, group = group.type, qty = availBlood[group], size = 38.dp)
                                    }
                                }
                            }
                        })
                        Spacer(modifier = Modifier.height(12.dp))

                        Row{
                            Image(
                                painter= painterResource(id =R.drawable.ic_plasma),
                                modifier= Modifier.size(33.dp),
                                contentDescription = null,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text= "Available Plasma"
                            )
                        }
                        Spacer(Modifier.height(8.dp))

                        LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                            val availPlasma = uiState.plasma.getQuantityMap()
                            items(availPlasma.keys.toList()){group ->
                                if(availPlasma[group]!! > 0 ){
                                    Box(
                                        Modifier.clickable {
                                            viewModel.showFluidInfoDialog( LifeFluids.PLASMA, fluidPlasmaGroup = group )
                                        }
                                    ){
                                        GroupLabel( plasma = group.type, qty = availPlasma[group], size = 38.dp)
                                    }
                                }
                            }
                        })

                        Spacer(modifier = Modifier.height(12.dp))

                        Row{
                            Image(
                                painter= painterResource(id =R.drawable.ic_plasma),
                                modifier= Modifier.size(33.dp),
                                contentDescription = null,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text= "Available Platelets"
                            )
                        }
                        Spacer(Modifier.height(8.dp))

                        LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                            val availPlatelets = uiState.platelets.getQuantityMap()
                            items(availPlatelets.keys.toList()){group ->
                                if(availPlatelets[group]!! > 0 ){
                                    Box(
                                        Modifier.clickable {
                                            viewModel.showFluidInfoDialog( LifeFluids.BLOOD, fluidPlateletsGroup = group )
                                        }
                                    ){
                                        GroupLabel(type = LifeFluids.PLATELETS, group = group.type, qty = availPlatelets[group], size = 38.dp)
                                    }
                                }
                            }
                        })

                    }else{
                        Text("Not Found :(")
                    }

                }

            }
        )

        if(uiState.showFluidDialog){

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x20000000))){
                FluidInfoDialog(
                    fluidType = uiState.dialogFluidType!!,
                    canDonateTo = if(uiState.dialogPlateletsGroup != null ) uiState.dialogPlateletsGroup.canDonateTo
                                    else uiState.dialogBloodGroup?.canDonateTo ?: emptyList(),

                    canReceivefrom = if(uiState.dialogPlateletsGroup != null ) uiState.dialogPlateletsGroup.canReceiveFrom
                                        else uiState.dialogBloodGroup?.canReceiveFrom ?: emptyList(),

                    onDismissReq = { viewModel.dismissFluidDialog() },
                    canRecPlasmaFrom = uiState.dialogPlasmaGroup?.canReceiveFrom ?: emptyList(),
                    canDonPlasmaTo = uiState.dialogPlasmaGroup?.canDonateTo ?: emptyList(),
                    bloodGroup = uiState.dialogBloodGroup,
                    plasmaGroup = uiState.dialogPlasmaGroup,
                    plateletsGroup = uiState.dialogPlateletsGroup
                )
            }

        }

    }

}

@Composable
private fun FluidInfoDialog(
    fluidType: LifeFluids,
    canDonateTo: List<BloodGroups>,
    canReceivefrom: List<BloodGroups>,
    canRecPlasmaFrom: List<Plasma>,
    canDonPlasmaTo: List<Plasma>,
    bloodGroup: BloodGroupsInfo? = null,
    plasmaGroup: PlasmaGroupInfo? = null,
    plateletsGroup: PlateletsGroupInfo? = null,
    onDismissReq: () -> Unit
){

    Dialog(
        onDismissRequest = onDismissReq
    ){

        Column(
            modifier= Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ){

            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                when(fluidType){
                    LifeFluids.PLASMA -> {
                        GroupLabel(plasma = plasmaGroup!!.type, size = 32.dp)
                    }
                    LifeFluids.BLOOD -> {
                        GroupLabel(group = bloodGroup!!.type, size = 32.dp, type = LifeFluids.BLOOD)
                    }
                    LifeFluids.PLATELETS -> {
                        GroupLabel(group = plateletsGroup!!.type, size = 32.dp, type = LifeFluids.PLATELETS)
                    }
                }
                Spacer(Modifier.width(4.dp))
                Text("Fluid Info", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier= Modifier.weight(1f))
                IconButton(icon = Icons.Rounded.Close, onClick = { onDismissReq() })
            }
            Spacer(Modifier.height(12.dp))

            Text(
                text= "Can Donate To: ",
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
                when(fluidType){
                    LifeFluids.PLASMA -> {
                        items(canDonPlasmaTo){
                            GroupLabel(plasma = it, size = 20.dp)
                        }
                    }
                    LifeFluids.BLOOD, LifeFluids.PLATELETS -> {
                        items(canDonateTo){
                            GroupLabel(group = it, size = 20.dp, type = fluidType)
                        }
                    }
                }

            })
            Spacer(Modifier.height(8.dp))

            Text(
                text= "Can Receive From: ",
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
                when(fluidType){
                    LifeFluids.PLASMA -> {
                        items(canRecPlasmaFrom){
                            GroupLabel(plasma = it, size = 20.dp)
                        }
                    }
                    LifeFluids.BLOOD, LifeFluids.PLATELETS -> {
                        items(canReceivefrom){
                            GroupLabel(group = it, size = 20.dp, type = fluidType)
                        }
                    }
                }

            })


        }

    }




}
