package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.LifeFluids.PLASMA
import com.pratyaksh.healthykingdom.utils.LifeFluids.BLOOD
import com.pratyaksh.healthykingdom.utils.LifeFluids.PLATELETS
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo

@Composable
fun GroupLabel(
    type: LifeFluids,
    group: BloodGroups
){
    val bgColor: Color =  when(type){

        BLOOD -> Color(0xFFFF2B2B)
        PLATELETS -> Color(0xFF8D1414)
        else -> Color(0xFF090909)

    }

    val text = when(group){

            BloodGroups.A_POSITIVE -> "A+"
            BloodGroups.A_NEGATIVE -> "A-"
            BloodGroups.B_POSITIVE -> "B+"
            BloodGroups.B_NEGATIVE -> "B-"
            BloodGroups.AB_POSITIVE -> "AB+"
            BloodGroups.AB_NEGATIVE -> "AB-"
            BloodGroups.O_POSITIVE -> "O+"
            BloodGroups.O_NEGATIVE -> "O-"
            BloodGroups.ERROR_TYPE -> "!"


        }

    Box(
        modifier=Modifier.height(20.dp).wrapContentWidth()
            .clip(RoundedCornerShape(4.dp))
            .padding(2.dp)
            .background(color = bgColor),
        contentAlignment = Alignment.Center
    ){
        Text(
            text= text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight= FontWeight.Black,
        )
    }

}

@Composable
fun GroupLabel(
    plasma: Plasma
){
    val bgColor = Color(0xFFD89918)

    val text: String = when(plasma){
            PlasmaGroupInfo.PLASMA_A -> "A"
            PlasmaGroupInfo.PLASMA_B -> "B"
            PlasmaGroupInfo.PLASMA_AB -> "AB"
            PlasmaGroupInfo.PLASMA_O -> "O"
        else -> { "!" }
    }

    Box(
        modifier=Modifier.size(20.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color = bgColor),
        contentAlignment = Alignment.Center
    ){
        Text(
            text= text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight= FontWeight.Black,
        )
    }

}