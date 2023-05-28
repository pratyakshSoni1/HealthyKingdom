package com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pratyaksh.healthykingdom.utils.*

data class MarkerDetailSheetUiState @OptIn(ExperimentalMaterialApi::class) constructor(
    val sheetState: BottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed),
    val isSheetCollapsed: Boolean = true,
    val sheetPeekState: Dp = 0.dp,
    val isLoading: Boolean= false,
    val isError: Boolean= false,
    val hospitalName: String = "",
    val hospitalId: String = "",
    val availBloodTypes: List<BloodGroupsInfo> = listOf(),
    val availPlasmaTypes: List<PlasmaGroupInfo> = listOf(),
    val availPlateletsTypes: List<PlateletsGroupInfo> = listOf()
)