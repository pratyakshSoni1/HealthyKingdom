package com.pratyaksh.healthykingdom.domain.model

import com.pratyaksh.healthykingdom.data.dto.AvailFluidsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toBloodModel
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toPlasmaModel
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toPlateletsModel
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toBloodDto
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toPlasmaDto
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toPlateletsDto

data class AvailFluids (

    val bloods: AvailBlood,
    val plasma: AvailPlasma,
    val platelets: AvailPlatelets,

    )


fun AvailFluids.toAvailFluidsDto(): AvailFluidsDto{
    return AvailFluidsDto(
        bloods.toBloodDto(),
        plasma.toPlasmaDto(),
        platelets.toPlateletsDto()
    )
}