package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun AppTextField(
    value: State<String>,
    onValueChange:(newValue: String) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    hint: String= ""
){

    TextField(
        value = value.value,
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
            .background(Color(0xFFD6D6D6))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.Transparent,
            cursorColor = Color.Blue
        )
    )


}