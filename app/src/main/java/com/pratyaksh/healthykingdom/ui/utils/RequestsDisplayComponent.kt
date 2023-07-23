package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

@Composable
fun RequestsDisplayComponent(
    requests: Requests?,
    onBloodGroupClick:(BloodGroupsInfo)->Unit,
    onPlateletsGroupClick:(PlateletsGroupInfo)->Unit,
    onPlasmaGroupClick:(PlasmaGroupInfo)->Unit,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x4DFFBF00))
            .padding(8.dp)

    ) {
        if (requests != null) {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){

                    Image(
                        painter = painterResource(id = R.drawable.requests),
                        modifier = Modifier.size(33.dp),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Requests",
                        modifier = Modifier
                            .padding(top = 6.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(Modifier.height(8.dp))
                FluidGroupList(
                    type = LifeFluids.BLOOD,
                    fluidsAvailable = requests.blood.map { it.type },
                    onBloodClick = { onBloodGroupClick(it) }
                )
                Spacer(Modifier.height(6.dp))
                FluidGroupList(
                    type = LifeFluids.PLATELETS,
                    fluidsAvailable = requests.platelets.map{it.type},
                    onPlateletsClick = {
                        onPlateletsGroupClick( it )
                    }
                )
                Spacer(Modifier.height(6.dp))
                FluidGroupList(
                    plasmaAvailable = requests.plasma,
                    onClick= { onPlasmaGroupClick(it) }
                )
                Spacer(Modifier.height(8.dp))
            }
        } else {
            Text(
                text = "No Requests",
                fontSize = 18.sp,
                color = Color(0x3E000000),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }

}