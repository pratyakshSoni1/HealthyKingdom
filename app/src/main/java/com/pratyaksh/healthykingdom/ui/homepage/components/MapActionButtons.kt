package com.pratyaksh.healthykingdom.ui.homepage.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.ui.utils.VisibilityIconButton

@Preview(showBackground = true)
@Composable
fun MapActionButtons(){

    Box(
        modifier= Modifier
            .fillMaxSize()
            .padding(end = 16.dp, bottom = 26.dp),
        contentAlignment = Alignment.BottomEnd
    ){

        Column{

            VisibilityIconButton(
                Icons.Default.Home,
                onClick = { Log.d("UI_LOGS", "Clicked Icon button") },
                backgroundColor = Color.White,
                iconColor = Color.Red
            )
            Spacer(modifier = Modifier.height(6.dp))
            
            VisibilityIconButton(
                icon = Icons.Default.LocationOn ,
                onClick = { Log.d("UI_LOGS", "Clicked Icon button") },
                isVisible = false,
                iconColor = Color.White,
                backgroundColor = Color.Magenta
            )

        }

    }

}