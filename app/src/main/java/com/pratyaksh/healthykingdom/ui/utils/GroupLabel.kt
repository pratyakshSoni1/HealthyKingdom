package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.Dp
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
    group: BloodGroups,
    qty: Int? = null,
    size: Dp = 20.dp
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


        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier= Modifier
                .height(size)
                .wrapContentWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(color = bgColor)
                .padding(horizontal = 2.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text= text,
                color = Color.White,
                fontSize = if(size < 35.dp) 14.sp else 18.sp,
                fontWeight= FontWeight.Black,
            )
        }
        if(qty != null) {
            Spacer(Modifier.height(2.dp))
            Text(text = qty.toString() + "L")
        }
    }

}

@Composable
fun GroupLabel(
    plasma: Plasma,
    qty: Int? = null,
    size: Dp = 20.dp
){
    val bgColor = Color(0xFFD89918)

    val text: String = when(plasma){
            Plasma.PLASMA_A -> "A"
            Plasma.PLASMA_B -> "B"
            Plasma.PLASMA_AB -> "AB"
            Plasma.PLASMA_O -> "O"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color = bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = if(size < 35.dp) 14.sp else 18.sp,
                fontWeight = FontWeight.Black,
            )
        }
        if(qty != null) {
            Spacer(Modifier.height(2.dp))
            Text(text = qty.toString() + "L")
        }
    }

}