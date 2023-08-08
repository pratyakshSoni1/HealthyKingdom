package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ErrorDialog(
    text: String,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = { onClose() }) {

        Box(
            Modifier
                .height(240.dp)
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(vertical = 2.dp, horizontal = 4.dp)
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(icon = Icons.Rounded.Close, onClick = { onClose() })
            }
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {


                Icon(
                    imageVector = Icons.Rounded.Warning,
                    tint = Color.Red,
                    modifier = Modifier.fillMaxSize(0.4f),
                    contentDescription = text
                )
                Spacer(Modifier.height(4.dp))

                Text(
                    text = text,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )
            }

        }
    }

}