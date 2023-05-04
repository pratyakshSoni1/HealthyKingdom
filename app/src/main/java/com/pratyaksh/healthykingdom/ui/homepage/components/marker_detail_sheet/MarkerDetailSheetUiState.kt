package com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet

import com.pratyaksh.healthykingdom.utils.*

data class MarkerDetailSheetUiState (
    val isLoading: Boolean,
    val hospitalName: String = "",
    val hospitalId: String = "",
    val availBloodTypes: List<BloodGroupsInfo> = listOf(),
    val availPlasmaTypes: List<PlasmaGroupInfo> = listOf(),
    val availPlateletsTypes: List<PlateletsGroupInfo> = listOf()
)