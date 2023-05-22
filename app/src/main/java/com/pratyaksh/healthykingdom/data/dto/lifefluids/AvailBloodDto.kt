package com.pratyaksh.healthykingdom.data.dto.lifefluids

import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood

data class AvailBloodDto(
    val a_pos: Int = 0,
    val a_neg: Int = 0,
    val ab_pos: Int = 0,
    val ab_neg: Int = 0,
    val b_pos: Int = 0,
    val b_neg: Int = 0,
    val o_pos: Int = 0,
    val o_neg: Int = 0,
)

fun AvailBloodDto.toBloodModel(): AvailBlood{
    return AvailBlood(
        a_pos,
        a_neg,
        ab_pos,
        ab_neg,
        b_pos,
        b_neg,
        o_pos,
        o_neg
    )
}
