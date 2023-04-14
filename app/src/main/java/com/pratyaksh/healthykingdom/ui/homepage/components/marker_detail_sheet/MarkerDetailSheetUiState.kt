package com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet

import com.pratyaksh.healthykingdom.utils.*

data class MarkerDetailSheetUiState (
    val isLoading: Boolean,
    val hospitalName: String = "",
    val availBloodTypes: List<BloodGroupInterface> = listOf(),
    val availPlasmaTypes: List<Plasma> = listOf(),
    val availPlateletsTypes: List<Platelets> = listOf()
)