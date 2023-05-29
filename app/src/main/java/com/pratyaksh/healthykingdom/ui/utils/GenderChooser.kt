package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.utils.Gender

@Composable
fun GenderChooser(
    onGenderChange: (type: Gender) -> Unit,
    selected: Gender,
    textColor: Color = Color.White,
    backgroundColor: Color = Color(0x80007BFF)
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .background(Color(0xFFE4E4E4))
            .padding(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp)
        ) {
            Spacer(Modifier.width(8.dp))

            Text(
                "Gender ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clip(RoundedCornerShape(100.dp)).background(backgroundColor)
            ) {
                GenderOption(
                    onClick = onGenderChange,
                    type = Gender.MALE,
                    txtColor = if (selected == Gender.MALE) backgroundColor else textColor,
                    bgColor = if (selected == Gender.MALE) textColor else Color.Transparent
                )

                GenderOption(
                    onClick = onGenderChange,
                    type = Gender.FEMALE,
                    txtColor = if (selected == Gender.FEMALE) backgroundColor else textColor,
                    bgColor = if (selected == Gender.FEMALE) textColor else Color.Transparent
                )
                GenderOption(
                    onClick = onGenderChange,
                    type = Gender.OTHERS,
                    txtColor = if (selected == Gender.OTHERS) backgroundColor else textColor,
                    bgColor = if (selected == Gender.OTHERS) textColor else Color.Transparent
                )
            }
        }
    }

}

@Composable
fun GenderOption(onClick: (type: Gender) -> Unit, type: Gender, txtColor: Color, bgColor: Color) {

    Text(
        text = when (type) {
            Gender.MALE -> "Male"
            Gender.FEMALE -> "Female"
            Gender.OTHERS -> "Others"
        },
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onClick(type)
            }
            .background(bgColor)
            .padding(vertical = 8.dp, horizontal = 6.dp)
            ,
        color = txtColor,
        fontWeight = FontWeight.Bold
    )

}