package com.pratyaksh.healthykingdom.utils



class PlasmaGroupInfo {
    object PLASMA_A: Plasma{
        override val type: String = "A"
        override val canDonateTo: List<Plasma> = listOf<Plasma>(PLASMA_O, PLASMA_A)
        override val canReceiveFrom: List<Plasma> = listOf<Plasma>(PLASMA_A, PLASMA_AB)
    }

    object PLASMA_B: Plasma{
        override val type: String = "B"
        override val canDonateTo: List<Plasma> = listOf<Plasma>(PLASMA_O, PLASMA_B)
        override val canReceiveFrom: List<Plasma> = listOf<Plasma>(PLASMA_B, PLASMA_AB)
    }

    object PLASMA_O: Plasma{
        override val type: String = "O"
        override val canDonateTo: List<Plasma> = listOf<Plasma>( PLASMA_O )
        override val canReceiveFrom: List<Plasma> = listOf<Plasma>( PLASMA_O, PLASMA_A, PLASMA_B, PLASMA_AB )
    }

    object PLASMA_AB: Plasma{
        override val type: String = "AB"
        override val canDonateTo: List<Plasma> = listOf<Plasma>(PLASMA_O, PLASMA_A, PLASMA_B, PLASMA_AB)
        override val canReceiveFrom: List<Plasma> = listOf<Plasma>(PLASMA_AB)
    }


}

interface Plasma{
        val type: String
        val canDonateTo: List<Plasma>
        val canReceiveFrom: List<Plasma>
}