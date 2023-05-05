package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pratyaksh.healthykingdom.utils.Gender

@Composable
fun GenderChooser(
    onGenderChange:(type: Gender) -> Unit,
    selected: Gender
){

    Box{
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier= Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF007BFF))
                .padding(vertical = 6.dp, horizontal = 4.dp)
        ){
            GenderOption(onClick = onGenderChange, type = Gender.MALE, color = if(selected == Gender.MALE) Color.Black else Color.Transparent)
            GenderOption(onClick = onGenderChange, type = Gender.FEMALE, color = if(selected == Gender.FEMALE) Color(0xFFFF00F3) else Color.Transparent)
            GenderOption(onClick = onGenderChange, type = Gender.OTHERS, color = if(selected == Gender.OTHERS) Color(0xFF00FF22) else Color.Transparent)
        }
    }

}

@Composable
fun GenderOption( onClick:(type: Gender)->Unit, type: Gender, color: Color){

    Text(
        text= when(type){
            Gender.MALE -> "Male"
            Gender.FEMALE -> "Female"
            Gender.OTHERS -> "Others"
        },
        modifier= Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(vertical = 2.dp, horizontal = 4.dp)
            .clickable {
                onClick(type)
            },
        color= Color.White,
        fontWeight= FontWeight.Bold
    )

}