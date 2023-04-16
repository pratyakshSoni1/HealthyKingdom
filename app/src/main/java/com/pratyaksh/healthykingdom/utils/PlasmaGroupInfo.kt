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

    object ERROR_TYPE: Plasma{
        override val type: String = "ERROR_TYPE"
        override val canDonateTo: List<Plasma> = listOf<Plasma>()
        override val canReceiveFrom: List<Plasma> = listOf<Plasma>()
    }


}

interface Plasma{
        val type: String
        val canDonateTo: List<Plasma>
        val canReceiveFrom: List<Plasma>
}


//fun BloodGroups.toPlasma(): Plasma{
//    return when(this){
//        BloodGroups.A_POSITIVE -> PlasmaGroupInfo.PLASMA_A
//        BloodGroups.A_NEGATIVE -> PlasmaGroupInfo.PLASMA_A
//        BloodGroups.AB_POSITIVE -> PlasmaGroupInfo.PLASMA_AB
//        BloodGroups.AB_NEGATIVE -> PlasmaGroupInfo.PLASMA_AB
//        BloodGroups.B_POSITIVE -> PlasmaGroupInfo.PLASMA_B
//        BloodGroups.B_NEGATIVE -> PlasmaGroupInfo.PLASMA_B
//        BloodGroups.O_POSITIVE -> PlasmaGroupInfo.PLASMA_B
//        BloodGroups.O_NEGATIVE -> PlasmaGroupInfo.PLASMA_B
//    }
//}
