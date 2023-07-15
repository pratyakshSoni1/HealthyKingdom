package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo
import com.pratyaksh.healthykingdom.utils.toPlateletsGroupInfo


@Composable
fun FluidGroupList(
    type: LifeFluids,
    fluidsAvailable: List<BloodGroupsInfo> ,
    onBloodClick:(BloodGroupsInfo)->Unit = { Unit },
    onPlateletsClick:(PlateletsGroupInfo)->Unit = { Unit }
) {

    var painterResId: Int
    var description: String

    when (type) {
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
    ) {
        Image(
            painter = painterResource(painterResId),
            contentDescription = description,
            modifier = Modifier.size(43.dp)
        )

        Text(
            text = " :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.width(8.dp))
        if (fluidsAvailable.isNotEmpty()) {
            for (fluid in fluidsAvailable) {
                Box(
                    modifier= Modifier.clickable {
                        if(type == LifeFluids.PLATELETS) onPlateletsClick(fluid.type.toPlateletsGroupInfo())
                        else onBloodClick(fluid)
                    }
                ){
                    GroupLabel(type = type, group = fluid.type)
                }
                Spacer(Modifier.width(8.dp))
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_visibility),
                contentDescription = "not available",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
        }
    }


}

@Composable
fun FluidGroupList(plasmaAvailable: List<PlasmaGroupInfo>, onClick:(PlasmaGroupInfo)->Unit = { Unit }) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(R.drawable.ic_plasma),
            contentDescription = "available plasma groups",
            modifier = Modifier.size(43.dp)
        )
        Text(
            text = " :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.width(8.dp))
        if (plasmaAvailable.isNotEmpty()) {
            for (fluid in plasmaAvailable) {
                Box(
                    modifier= Modifier.clickable { onClick(fluid) }
                ) {
                    GroupLabel(plasma = fluid.type)
                }
                Spacer(Modifier.width(8.dp))
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_visibility),
                contentDescription = "not available",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )

        }
    }


}