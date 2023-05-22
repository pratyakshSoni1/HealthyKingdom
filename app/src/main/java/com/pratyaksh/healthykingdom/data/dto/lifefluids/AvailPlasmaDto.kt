package com.pratyaksh.healthykingdom.data.dto.lifefluids

import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma

data class AvailPlasmaDto(
    val a_group: Int = 0,
    val ab_group: Int = 0,
    val b_group: Int = 0,
    val o_group: Int = 0,
)

fun AvailPlasmaDto.toPlasmaModel(): AvailPlasma {
    return AvailPlasma(
        a_group,
        ab_group,
        b_group,
        o_group
    )
}
