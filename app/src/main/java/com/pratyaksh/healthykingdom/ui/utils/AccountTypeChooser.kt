package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Account Type: ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.width(4.dp))

        Box {
            Row(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(width = 2.5.dp, color= Color.Black, shape= RoundedCornerShape(8.dp))
                    .padding(horizontal = 4.dp)
                    .clickable { onToggleExpand() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Select account type"
                )
                AccTypeMenuItem(
                    name = accountType.type,
                    painterResource(id = accountType.img)
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
        modifier = Modifier.fillMaxWidth()
    ) {
        DropdownMenuItem(onClick = {
            onAccTypeChange(AccountTypes.AMBULANCE)
        }) {
            AccTypeMenuItem(
                name = "AMBULANCE",
                img = painterResource(id = R.drawable.ambulance)
            )
        }

        DropdownMenuItem(onClick = {
            onAccTypeChange(AccountTypes.HOSPITAL)
        }) {
            AccTypeMenuItem(
                name = "HOSPITAL",
                img = painterResource(id = R.drawable.hospital)
            )
        }

        DropdownMenuItem(onClick = {
            onAccTypeChange(AccountTypes.HOSPITAL)
        }) {
            AccTypeMenuItem(
                name = "Public User",
                img = painterResource(id = R.drawable.ic_person)
            )
        }
    }
}