package com.pratyaksh.healthykingdom.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.ui.utils.AppTextField

@Composable
fun VerifyPasswordDialog(
    pass: String,
    onPassChange: (newTxt: String) -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x4D000000)),
        contentAlignment= Alignment.Center
    ) {

        Column(
            Modifier.fillMaxWidth(0.9f).padding(horizontal = 12.dp).clip(RoundedCornerShape(12.dp)).background(Color.White)
        ) {

            Text(
                text = "Delete Account ?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            Text(
                text = "Confirm that it's really you trying deleting your account",
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            )

            AppTextField(
                value = pass,
                onValueChange = onPassChange,
                keyboard = KeyboardType.Password
            )
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "cancel", modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { onCancel() }, color = Color.DarkGray
                )
                Button(
                    onClick = { onDelete() },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }
            }
            Spacer(Modifier.height(12.dp))

        }

    }

}