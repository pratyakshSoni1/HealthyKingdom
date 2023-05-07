package com.pratyaksh.healthykingdom.domain.model

import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets

data class AvailFluids (

    val bloods: AvailBlood,
    val plasma: AvailPlasma,
    val platelets: AvailPlatelets,

    )