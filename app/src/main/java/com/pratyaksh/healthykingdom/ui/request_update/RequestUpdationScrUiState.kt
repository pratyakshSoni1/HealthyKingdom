package com.pratyaksh.healthykingdom.ui.request_update

import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

data class RequestUpdationScrUiState (
    val hospitalId: String? = null,
    val bloodRequest: MutableList<BloodGroupsInfo> = mutableListOf(),
    val plateletsRequest: MutableList<PlateletsGroupInfo> = mutableListOf(),
    val plasmaRequest: MutableList<PlasmaGroupInfo> = mutableListOf(),

    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorTxt: String = "",
    val isUpdationActive: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showGroupInfoDialog: Boolean = false,

    val dialogFluidType: LifeFluids? = null,
    val dialogBloodGroup: BloodGroupsInfo? = null,
    val dialogPlateletsGroup: PlateletsGroupInfo? = null,
    val dialogPlasmaGroup: PlasmaGroupInfo? = null,

    )