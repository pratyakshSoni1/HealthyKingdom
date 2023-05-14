package com.pratyaksh.healthykingdom.domain.model.lifefluids

import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto

data class AvailPlasma(
    val aGroup: Int,
    val abGroup: Int,
    val bGroup: Int,
    val oGroup: Int
): LifeFluidsModel()

fun AvailPlasma.toPlasmaDto(): AvailPlasmaDto {
    return AvailPlasmaDto(
        aGroup,
        abGroup,
        bGroup,
        oGroup
    )
}

