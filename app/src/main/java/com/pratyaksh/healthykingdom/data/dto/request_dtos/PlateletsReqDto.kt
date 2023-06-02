package com.pratyaksh.healthykingdom.data.dto.request_dtos

data class PlateletsReqDto (
    val a_pos: Boolean = false,
    val a_neg: Boolean = false,
    val ab_pos: Boolean = false,
    val ab_neg: Boolean = false,
    val b_pos: Boolean = false,
    val b_neg: Boolean = false,
    val o_pos: Boolean = false,
    val o_neg: Boolean = false
)