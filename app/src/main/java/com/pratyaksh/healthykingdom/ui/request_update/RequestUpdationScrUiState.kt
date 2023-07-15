package com.pratyaksh.healthykingdom.ui.request_update

import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

data class RequestUpdationScrUiState (
    val hospitalId: String? = null,
    val bloodRequest: List<BloodGroupsInfo> = emptyList(),
    val plateletsRequest: List<PlateletsGroupInfo> = emptyList(),
    val plasmaRequest: List<PlasmaGroupInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorTxt: String = "",
    val isUpdationActive: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showGroupInfoDialog: Boolean = false,
 )