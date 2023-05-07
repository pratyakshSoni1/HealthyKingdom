package com.pratyaksh.healthykingdom.data.dto.lifefluids

import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma

data class AvailPlasmaDto(
    val aGroup: Int = 0,
    val abGroup: Int = 0,
    val bGroup: Int = 0,
    val oGroup: Int = 0,
)

fun AvailPlasmaDto.toPlasmaModel(): AvailPlasma {
    return AvailPlasma(
        aGroup,
        abGroup,
        bGroup,
        oGroup
    )
}
