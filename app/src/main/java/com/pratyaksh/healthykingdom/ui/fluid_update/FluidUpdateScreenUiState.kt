package com.pratyaksh.healthykingdom.ui.fluid_update

import com.pratyaksh.healthykingdom.domain.model.AvailFluids
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets
import com.pratyaksh.healthykingdom.utils.LifeFluids

data class FluidUpdateScreenUiState (

    val fluidType: LifeFluids,
    val availBloodGroups: AvailBlood,
    val availPlasma: AvailPlasma,
    val currentUserId: String,

)