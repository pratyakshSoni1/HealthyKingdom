package com.pratyaksh.healthykingdom.data.dto.lifefluids

import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood

data class AvailBloodDto(
    val aPos: Int = 0,
    val aNeg: Int = 0,
    val abPos: Int = 0,
    val abNeg: Int = 0,
    val bPos: Int = 0,
    val bNeg: Int = 0,
    val oPos: Int = 0,
    val oNeg: Int = 0,
)

fun AvailBloodDto.toBloodModel(): AvailBlood{
    return AvailBlood(
        aPos,
        aNeg,
        abPos,
        abNeg,
        bPos,
        bNeg,
        oPos,
        oNeg
    )
}
