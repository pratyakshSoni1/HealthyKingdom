package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pratyaksh.healthykingdom.R

@Composable
fun IconButton(
    icon: ImageVector,
    iconColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit,
    contentDescription: String? = null,
    size: Dp = 12.dp
) {

    androidx.compose.material.IconButton(
        onClick = { onClick() },
    ) {
        Image(
            imageVector = icon,
            contentDescription = contentDescription,
            Modifier
                .clip(CircleShape)
                .background(backgroundColor)
                .padding(size),
            colorFilter = ColorFilter.tint(iconColor),
        )
    }


}

@Composable
fun IconButton(
    icon: Painter,
    iconColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit,
    contentDescription: String? = null
) {

    androidx.compose.material.IconButton(
        onClick = { onClick() },
    ) {
        Image(
            painter = icon,
            contentDescription = contentDescription,
            Modifier
                .clip(CircleShape)
                .background(backgroundColor)
                .padding(12.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }


}

@Composable
fun VisibilityIconButton(
    icon: Painter,
    isVisible: Boolean = true,
    backgroundColor: Color = Color.White,
    onClick: (isEnabled: Boolean) -> Unit,
    contentDescription: String? = null
) {

    val isIconEnabled = remember { mutableStateOf<Boolean>(isVisible) }

    androidx.compose.material.IconButton(
        onClick = {
            isIconEnabled.value = !isIconEnabled.value
            onClick(isIconEnabled.value)
        },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = icon,
                contentDescription = contentDescription,
                Modifier
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .padding(12.dp)
            )
            if (isIconEnabled.value) {
                Image(
                    painter = painterResource(id = R.drawable.ic_visibility),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    colorFilter = ColorFilter.tint(Color(0xFFFF4A4A))
                )
            }
        }

    }
}

@Composable
fun VisibilityIconButton(
    icon: ImageVector,
    isVisible: Boolean = true,
    iconColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    onClick: (isenabled: Boolean) -> Unit,
    contentDescription: String? = null
) {
    val isIconEnabled = remember { mutableStateOf<Boolean>(isVisible) }

    androidx.compose.material.IconButton(
        onClick = {
            isIconEnabled.value = !isIconEnabled.value
            onClick(isIconEnabled.value)
        },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                imageVector = icon,
                contentDescription = contentDescription,
                Modifier
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .padding(12.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
            if (isIconEnabled.value) {
                Image(
                    painter = painterResource(id = R.drawable.ic_visibility),
                    contentDescription = contentDescription,
                    Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    colorFilter = ColorFilter.tint(Color(0xFFFF4A4A))
                )
            }
        }

    }


}
