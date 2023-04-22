package com.pratyaksh.healthykingdom.ui.hospital_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.data.dto.toMapsGeopoint
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteFirebaseRepo
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.ui.utils.MapLocationPreview
import javax.inject.Inject

@Composable
fun HospitalDetailsScreen(
    navController: NavHostController,
    hospitalId: String,
    viewModel: HospitalDetailsVM = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchHospital(hospitalId)
    })

    Scaffold(
        modifier= Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        topBar = {
            Row{
                IconButton(icon = Icons.Rounded.ArrowBack, onClick = { navController.popBackStack() })
                Spacer(Modifier.width(12.dp))
                Text(
                    "Details",
                    fontWeight= FontWeight.Bold,
                    color = Color.Black
                )
            }
        },
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ){
                if(viewModel.hospital.value != null) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Image(
                            painter = painterResource(id = R.drawable.hospital),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    MapLocationPreview(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        location = viewModel.hospital.value!!.location.toMapsGeopoint(),
                        name = viewModel.hospital.value!!.name
                    )
                }else{
                    Text("Not Found :(")
                }

            }

        }
    )

}