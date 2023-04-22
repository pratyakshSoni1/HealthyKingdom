package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.ui.hospital_registration.RegistrationTextFieldsState
import kotlin.math.max

@Composable
fun AppTextField(
    value: String,
    onValueChange:(newValue: String) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    hint: String= ""
){

    TextField(
        value = value,
        placeholder = {
            Text(hint)
        },
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboard
        ),
        maxLines = maxLines,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE6E6E6))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.Transparent,
            cursorColor = Color.Blue,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )


}


@Composable
fun OtpTextField(
    text: String
){

    val digit1 = if(text.length > 0) "${text[0]}" else ""
    val digit2 = if(text.length > 1) "${text[1]}" else ""
    val digit3 = if(text.length > 2) "${text[2]}" else ""
    val digit4 = if(text.length > 3) "${text[3]}" else ""
    val digit5 = if(text.length > 4) "${text[4]}" else ""
    val digit6 = if(text.length > 5) "${text[5]}" else ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ){

        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            Text(
                text= digit1,
                fontSize = 22.sp,
                color= Color.Black,
                maxLines = 1,
                fontWeight= FontWeight.Bold,
                modifier= Modifier
                    .width(36.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD6D6D6)),
                textAlign = TextAlign.Center
            )

            Text(
                text= digit2,
                fontSize = 22.sp,
                color= Color.Black,
                maxLines = 1,
                fontWeight= FontWeight.Bold,
                modifier= Modifier
                    .width(36.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD6D6D6)),
                textAlign = TextAlign.Center
            )

            Text(
                text=digit3,
                fontSize = 22.sp,
                color= Color.Black,
                maxLines = 1,
                fontWeight= FontWeight.Bold,
                modifier= Modifier
                    .width(36.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD6D6D6)),
                textAlign = TextAlign.Center
            )

            Text(
                text=digit4,
                fontSize = 22.sp,
                color= Color.Black,
                maxLines = 1,
                fontWeight= FontWeight.Bold,
                modifier= Modifier
                    .width(36.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD6D6D6)),
                textAlign = TextAlign.Center
            )

            Text(
                text=digit5,
                fontSize = 22.sp,
                color= Color.Black,
                maxLines = 1,
                fontWeight= FontWeight.Bold,
                modifier= Modifier
                    .width(36.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD6D6D6)),
                textAlign = TextAlign.Center
            )

            Text(
                text=digit6,
                fontSize = 22.sp,
                color= Color.Black,
                maxLines = 1,
                fontWeight= FontWeight.Bold,
                modifier= Modifier
                    .width(36.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD6D6D6)),
                textAlign = TextAlign.Center
            )

        }

    }


}