package com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.ui.utils.GroupLabel
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.getOnlyGroup
import com.pratyaksh.healthykingdom.ui.utils.IconButton

@Composable
fun MarkerDetailsSheet(
    uiState: MarkerDetailSheetUiState,
    onCloseClick:()->Unit
){

    Column(
        modifier= Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp, top = 8.dp, start = 14.dp, end = 14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ){
        Box(modifier=Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Box(
                Modifier
                    .width(64.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray))

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    icon = Icons.Rounded.Close,
                    onClick = { onCloseClick() },
                    iconColor = Color.LightGray
                )
            }
        }
        if(uiState.isLoading){
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 26.dp, start= 14.dp, end= 14.dp),
                color= Color.Blue
            )
        }else{
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Image(
                    painter = painterResource(id = R.drawable.hospital),
                    contentDescription = uiState.hospitalName,
                    modifier= Modifier.size(48.dp)
                )

                Spacer(modifier = (Modifier.width(8.dp)))
                Text(
                    text= uiState.hospitalName,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text= "Available Life-Fluids",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp,
                modifier= Modifier
                    .background(Color.Green)
                    .clip(RoundedCornerShape(2.dp))
                    .padding(vertical = 4.dp, horizontal = 6.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            FluidGroupList(type = LifeFluids.BLOOD, fluidsAvailable = uiState.availBloodTypes.getOnlyGroup() )
            Spacer(modifier = Modifier.height(8.dp))
            FluidGroupList(plasmaAvailable = uiState.availPlasmaTypes )
            Spacer(modifier = Modifier.height(8.dp))
            FluidGroupList(type = LifeFluids.PLATELETS, fluidsAvailable = uiState.availPlateletsTypes.getOnlyGroup() )
            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Button(
                    onClick={  },
                    modifier= Modifier.fillMaxWidth(0.45f),
                    colors = ButtonDefaults.buttonColors(Color.Transparent ),
                    elevation = ButtonDefaults.elevation(0.dp, pressedElevation = 0.dp)
                ){
                    Text( text= "Close", color= Color.Red )
                }

                Button(
                    onClick={  },
                    modifier= Modifier.fillMaxWidth(0.45f),
                    colors = ButtonDefaults.buttonColors(Color(0xFF00A5F0) )
                ){
                    Text( text= "Details", color= Color.White  )
                }

            }



        }

    }

}

@Composable
private fun FluidGroupList(type: LifeFluids, fluidsAvailable: List<BloodGroups>){

    var painterResId: Int
    var description: String

    when(type){
        LifeFluids.PLASMA -> {
            painterResId = R.drawable.ic_plasma
            description = "available plasma groups"
        }

        LifeFluids.BLOOD -> {
            painterResId = R.drawable.ic_blood
            description = "available plasma groups"
        }

        LifeFluids.PLATELETS -> {
            painterResId = R.drawable.ic_platelets
            description = "available platelet groups"
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Image(
            painter = painterResource( painterResId ),
            contentDescription = description,
            modifier = Modifier.size(43.dp)
        )

        Text(
            text=" :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color= Color.Black
        )
        Spacer(Modifier.width(8.dp))
        if(fluidsAvailable.isNotEmpty()){
            for( fluid in fluidsAvailable ){
                GroupLabel(type = type, group = fluid )
                Spacer(Modifier.width(8.dp))
            }
        }else{
            Icon(
                painter = painterResource( R.drawable.ic_visibility ),
                contentDescription = "not available",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
        }
    }


}

@Composable
private fun FluidGroupList(plasmaAvailable: List<Plasma>){

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Image(
            painter = painterResource( R.drawable.ic_plasma ),
            contentDescription = "available plasma groups",
            modifier = Modifier.size(43.dp)
        )
        Text(
            text=" :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color= Color.Black
        )
        Spacer(Modifier.width(8.dp))
        if(plasmaAvailable.isNotEmpty()){
            for( fluid in plasmaAvailable ){
                GroupLabel( plasma = fluid )
                Spacer(Modifier.width(8.dp))
            }
        }else{
            Icon(
                painter = painterResource( R.drawable.ic_visibility ),
                contentDescription = "not available",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )

        }
    }


}

