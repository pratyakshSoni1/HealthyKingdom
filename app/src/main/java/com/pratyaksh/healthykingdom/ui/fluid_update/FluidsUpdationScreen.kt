package com.pratyaksh.healthykingdom.ui.fluid_update

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow

@Composable
fun FluidsUpdationScreen(
    navController: NavHostController,
    getCurrentLoggedUser:() -> Flow<Resource<String?>>,
    fluidType: LifeFluids?,
    viewModel: FluidUpdateScreenVM = hiltViewModel()
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    onBackPress = { navController.popBackStack() },
                    title = "Updtae ${fluidType?.name}"
                ){
                    Text(
                        text= "Update",
                        color= Color.Blue,
                        modifier=Modifier.clickable {
                            viewModel.onUpdateFluid()
                        }
                    )
                }
            }
        ) {
            Column(Modifier.padding(it)) {

            }
        }
    }

}