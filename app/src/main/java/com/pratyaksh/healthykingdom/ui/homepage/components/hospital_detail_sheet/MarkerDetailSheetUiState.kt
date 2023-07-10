package com.pratyaksh.healthykingdom.ui.homepage.components.hospital_detail_sheet

import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.utils.*

data class MarkerDetailSheetUiState (
    val isLoading: Boolean= false,
    val isError: Boolean= false,
    val hospitalName: String = "",
    val hospitalId: String = "",
    val availBloodTypes: List<BloodGroupsInfo> = listOf(),
    val availPlasmaTypes: List<PlasmaGroupInfo> = listOf(),
    val availPlateletsTypes: List<PlateletsGroupInfo> = listOf(),
    val requests: Requests? = null
)