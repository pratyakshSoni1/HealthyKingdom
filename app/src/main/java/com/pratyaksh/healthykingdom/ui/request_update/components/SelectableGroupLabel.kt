package com.pratyaksh.healthykingdom.ui.request_update.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratyaksh.healthykingdom.ui.utils.GroupLabel
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectableGroupLabel(
    fluidType: LifeFluids,
    group: BloodGroups,
    onShowGroupInfoDialog: () -> Unit,
    isSelected: Boolean,
    toggleSelection: () -> Unit
) {
    Box(
        Modifier
            .padding(vertical = 2.5.dp)
            .combinedClickable(
                enabled = true,
                onClick = { toggleSelection() },
                onLongClick = { onShowGroupInfoDialog() }
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        GroupLabel(
            type = fluidType,
            group = group,
            fontSize = 24.sp
        )
        if (isSelected)
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "selected",
                tint= Color(0xFF0027FF),
                modifier= Modifier.size(6.dp)
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectableGroupLabel(
    group: Plasma,
    onShowGroupInfoDialog: () -> Unit,
    isSelected: Boolean,
    toggleSelection: () -> Unit

) {
    Box(
        Modifier
            .padding(vertical = 2.5.dp)
            .combinedClickable(
                enabled = true,
                onClick = { toggleSelection() },
                onLongClick = { onShowGroupInfoDialog() }
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        GroupLabel(
            plasma = group,
            fontSize = 24.sp
        )

        if (isSelected)
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "selected",
                tint= Color(0xFF0027FF),
                modifier= Modifier.size(6.dp)
            )
    }
}