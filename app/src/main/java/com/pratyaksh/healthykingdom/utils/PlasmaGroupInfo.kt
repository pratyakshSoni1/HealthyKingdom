package com.pratyaksh.healthykingdom.utils

sealed class PlasmaGroupInfo(
    val type: String, val canDonateTo: List<Plasma>, val canReceiveFrom: List<Plasma>
) {
    object Plasma_A : PlasmaGroupInfo(
        type = "A",
        canDonateTo = listOf<Plasma>(Plasma.PLASMA_O, Plasma.PLASMA_A),
        canReceiveFrom = listOf<Plasma>(Plasma.PLASMA_A, Plasma.PLASMA_AB)
    )


    object Plasma_B : PlasmaGroupInfo(
        type = "B",
        canDonateTo = listOf<Plasma>(Plasma.PLASMA_O, Plasma.PLASMA_B),
        canReceiveFrom = listOf<Plasma>(Plasma.PLASMA_B, Plasma.PLASMA_AB)
    )

    object Plasma_O : PlasmaGroupInfo(
        type = "O",
        canDonateTo = listOf<Plasma>(Plasma.PLASMA_O),
        canReceiveFrom = listOf<Plasma>(
            Plasma.PLASMA_O,
            Plasma.PLASMA_A,
            Plasma.PLASMA_B,
            Plasma.PLASMA_AB
        )
    )

    object Plasma_AB : PlasmaGroupInfo(
        type = "AB",
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
