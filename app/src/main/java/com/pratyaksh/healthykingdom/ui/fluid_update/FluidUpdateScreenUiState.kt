package com.pratyaksh.healthykingdom.ui.fluid_update

import com.pratyaksh.healthykingdom.domain.model.AvailFluids
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets
import com.pratyaksh.healthykingdom.utils.LifeFluids

data class FluidUpdateScreenUiState (

    val fluidType: LifeFluids? = null,
    val availBloodGroups: AvailBlood = AvailBlood(0, 0, 0, 0, 0, 0, 0, 0),
    val availPlasma: AvailPlasma = AvailPlasma(0, 0, 0, 0,),
    val currentUserId: String? = null,
    val isLoading: Boolean= false,
    val errorText: String= "Unexpected Error Occured !",
    val showErrorDialog: Boolean= false,
    val showSuccesssDialog: Boolean= false,
    val isUpdateBtnActive: Boolean= false


    )