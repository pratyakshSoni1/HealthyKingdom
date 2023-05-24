package com.pratyaksh.healthykingdom.data.dto

import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toBloodModel
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toPlasmaModel
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toPlateletsModel
import com.pratyaksh.healthykingdom.domain.model.AvailFluids
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets

data class AvailFluidsDto (

    val bloods: AvailBloodDto? = null,
    val plasma: AvailPlasmaDto? = null,
    val platelets: AvailPlateletsDto? = null,

)

fun AvailFluidsDto.toAvailFluids(): AvailFluids{
    return AvailFluids(
        bloods?.toBloodModel() ?: AvailBlood(0, 0,0, 0, 0, 0, 0, 0),
        plasma?.toPlasmaModel() ?: AvailPlasma(0, 0, 0, 0),
        platelets?.toPlateletsModel() ?: AvailPlatelets(0, 0,0, 0, 0, 0, 0, 0)
    )
}