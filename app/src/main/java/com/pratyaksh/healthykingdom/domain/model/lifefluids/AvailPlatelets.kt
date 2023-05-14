package com.pratyaksh.healthykingdom.domain.model.lifefluids

import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto

data class AvailPlatelets(
    val aPos: Int ,
    val aNeg: Int ,
    val abPos: Int ,
    val abNeg: Int ,
    val bPos: Int ,
    val bNeg: Int ,
    val oPos: Int ,
    val oNeg: Int
): LifeFluidsModel()

fun AvailBlood.toPlateletsModel(): AvailPlatelets {
    return AvailPlatelets(
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

fun AvailPlatelets.toBloodsModel(): AvailBlood {
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

fun AvailPlatelets.toPlateletsDto(): AvailPlateletsDto{
    return AvailPlateletsDto(
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


