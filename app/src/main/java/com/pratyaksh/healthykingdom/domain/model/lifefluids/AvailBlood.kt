package com.pratyaksh.healthykingdom.domain.model.lifefluids

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
