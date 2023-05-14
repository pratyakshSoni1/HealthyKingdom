package com.pratyaksh.healthykingdom.domain.model.lifefluids

import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto

data class AvailBlood(
    val aPos: Int,
    val aNeg: Int,
    val abPos: Int,
    val abNeg: Int,
    val bPos: Int,
    val bNeg: Int,
    val oPos: Int,
    val oNeg: Int
): LifeFluidsModel()

fun AvailBlood.toBloodDto(): AvailBloodDto{
    return AvailBloodDto(
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

