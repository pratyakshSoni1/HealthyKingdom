package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerifyPasswordDialog(
    password: String,
    onPassTxtChange: (newtxt: String)->Unit,
    onVerify: ()->Unit,
    onClose: ()->Unit
){

    Column(
        modifier= Modifier
            .fillMaxWidth(0.95f)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(icon = Icons.Rounded.Lock, onClick = { /* Do Nothing :) */ }, iconColor = Color.Blue)
            Text(
                "Verfiy Yourself",
                fontSize= 16.sp,
                fontWeight = FontWeight.Bold,
                modifier= Modifier.weight(1f)
            )
            IconButton(icon = Icons.Rounded.Close, onClick = { onClose() })
        }
        Spacer(Modifier.height(4.dp))

        Text(
            "Enter Your Password",
            fontWeight = FontWeight.Bold,
            modifier= Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(2.dp))
        AppTextField(value = password, onValueChange = {
            onPassTxtChange(it)
        })
        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {
                onVerify()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF007BFF)
            ),
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(text = "Verify")
        }
        Spacer(modifier = Modifier.height(8.dp))

    }

}