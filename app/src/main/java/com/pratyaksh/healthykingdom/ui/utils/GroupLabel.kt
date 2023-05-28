package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.LifeFluids.BLOOD
import com.pratyaksh.healthykingdom.utils.LifeFluids.PLATELETS
import com.pratyaksh.healthykingdom.utils.Plasma

@Composable
fun GroupLabel(
    type: LifeFluids,
    group: BloodGroups,
    qty: Int? = null,
    fontSize: TextUnit = 16.sp
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
                .wrapContentSize()
                .padding( if(fontSize < 22.sp ) 3.dp else 4.dp )
                .clip(RoundedCornerShape(4.dp))
                .background(color = bgColor)
                .padding(horizontal = 2.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text= text,
                color = Color.White,
                fontSize = fontSize,
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
    fontSize: TextUnit = 16.sp
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
            modifier = Modifier.wrapContentSize()
                .padding( if(fontSize < 22.sp ) 3.dp else 4.dp )
                .clip(RoundedCornerShape(4.dp))
                .background(color = bgColor)
                .padding(horizontal= 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
            )
        }
        if(qty != null) {
            Spacer(Modifier.height(2.dp))
            Text(text = qty.toString() + "L")
        }
    }

}