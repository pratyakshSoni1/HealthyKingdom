package com.pratyaksh.healthykingdom.utils

sealed class PlasmaGroupInfo(
    val type: Plasma, val canDonateTo: List<Plasma>, val canReceiveFrom: List<Plasma>
) {
    object Plasma_A : PlasmaGroupInfo(
        type = Plasma.PLASMA_A,
        canDonateTo = listOf<Plasma>(Plasma.PLASMA_O, Plasma.PLASMA_A),
        canReceiveFrom = listOf<Plasma>(Plasma.PLASMA_A, Plasma.PLASMA_AB)
    )


    object Plasma_B : PlasmaGroupInfo(
        type = Plasma.PLASMA_B ,
        canDonateTo = listOf<Plasma>(Plasma.PLASMA_O, Plasma.PLASMA_B),
        canReceiveFrom = listOf<Plasma>(Plasma.PLASMA_B, Plasma.PLASMA_AB)
    )

    object Plasma_O : PlasmaGroupInfo(
        type = Plasma.PLASMA_O ,
        canDonateTo = listOf<Plasma>(Plasma.PLASMA_O),
        canReceiveFrom = listOf<Plasma>(
            Plasma.PLASMA_O,
            Plasma.PLASMA_A,
            Plasma.PLASMA_B,
            Plasma.PLASMA_AB
        )
    )

    object Plasma_AB : PlasmaGroupInfo(
        type = Plasma.PLASMA_AB ,
        canDonateTo = listOf<Plasma>(
            Plasma.PLASMA_O,
            Plasma.PLASMA_A,
            Plasma.PLASMA_B,
            Plasma.PLASMA_AB
        ),
        canReceiveFrom = listOf<Plasma>(Plasma.PLASMA_AB)
    )


}

enum class Plasma {
    PLASMA_O,
    PLASMA_A,
    PLASMA_B,
    PLASMA_AB
}

fun Plasma.toPlasmaGroupInfo() = when(this){
    Plasma.PLASMA_O -> PlasmaGroupInfo.Plasma_O
    Plasma.PLASMA_A -> PlasmaGroupInfo.Plasma_A
    Plasma.PLASMA_B -> PlasmaGroupInfo.Plasma_B
    Plasma.PLASMA_AB -> PlasmaGroupInfo.Plasma_AB
}
