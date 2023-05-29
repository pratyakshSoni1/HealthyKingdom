package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.utils.AccountTypes

@Composable
fun AccountTypeChooser(
    isExpanded: Boolean,
    onToggle:(expand: Boolean)->Unit,
    accountType: AccountTypes,
    onToggleExpand:()->Unit,
    onAccChange:(type: AccountTypes)->Unit
){
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp))
            .background(Color(0xFFE4E4E4))
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Text(
            "Account Type ",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier= Modifier.weight(1f)
        )
        Spacer(Modifier.width(4.dp))

        Box(
            modifier= Modifier
                .clip(RoundedCornerShape(100.dp)).background(Color(0x80007BFF)).padding(vertical = 2.dp)
        ) {
            Row(
                Modifier
                    .padding(horizontal = 12.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) { onToggleExpand() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AccTypeMenuItem(
                    name = accountType.type,
                    painterResource(id = accountType.img)
                )
                Spacer(Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Select account type",
                    tint = Color.White
                )
            }

            AccountTypeSpinner(
                isExpanded = isExpanded,
                onToggle = onToggle,
                onAccTypeChange = onAccChange
            )

        }
    }

}


@Composable
private fun AccountTypeSpinner(
    isExpanded: Boolean,
    onToggle:(expand: Boolean)->Unit,
    onAccTypeChange:(type: AccountTypes) -> Unit
){
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onToggle(false) },
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
    ) {
        DropdownMenuItem(onClick = {
            onToggle(false)
            onAccTypeChange(AccountTypes.AMBULANCE)
        }) {
            AccTypeMenuItem(
                name = "AMBULANCE",
                img = painterResource(id = R.drawable.ambulance)
            )
        }

        DropdownMenuItem(onClick = {
            onToggle(false)
            onAccTypeChange(AccountTypes.HOSPITAL)
        }) {
            AccTypeMenuItem(
                name = "HOSPITAL",
                img = painterResource(id = R.drawable.hospital)
            )
        }

        DropdownMenuItem(onClick = {
            onToggle(false)
            onAccTypeChange(AccountTypes.PUBLIC_USER)
        }) {
            AccTypeMenuItem(
                name = "Public User",
                img = painterResource(id = R.drawable.ic_person)
            )
        }
    }
}