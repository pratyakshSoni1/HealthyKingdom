package com.pratyaksh.healthykingdom.utils

sealed class PlateletsGroupInfo {

    object A_POSITIVE: Platelets{
        override val type: BloodGroups = BloodGroups.A_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_POSITIVE, BloodGroups.O_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_POSITIVE, BloodGroups.A_NEGATIVE, BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
    }

    object A_NEGATIVE: Platelets{
        override val type: BloodGroups = BloodGroups.A_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.O_POSITIVE, BloodGroups.A_POSITIVE, BloodGroups.A_NEGATIVE, BloodGroups.O_NEGATIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_NEGATIVE, BloodGroups.AB_NEGATIVE)
    }


    object B_POSITIVE: Platelets{
        override val type: BloodGroups = BloodGroups.B_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.O_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_POSITIVE, BloodGroups.B_NEGATIVE, BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
    }

    object B_NEGATIVE: Platelets{
        override val type: BloodGroups = BloodGroups.B_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_POSITIVE, BloodGroups.B_NEGATIVE, BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_NEGATIVE, BloodGroups.B_POSITIVE, BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
    }


    object O_POSITIVE: Platelets{
        override val type: BloodGroups = BloodGroups.O_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.O_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_POSITIVE, BloodGroups.B_NEGATIVE, BloodGroups.O_POSITIVE,BloodGroups.O_NEGATIVE, BloodGroups.A_POSITIVE, BloodGroups.A_NEGATIVE , BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
    }

    object O_NEGATIVE: Platelets{
        override val type: BloodGroups = BloodGroups.O_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>( BloodGroups.O_POSITIVE, BloodGroups.O_NEGATIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_NEGATIVE, BloodGroups.B_NEGATIVE, BloodGroups.AB_NEGATIVE, BloodGroups.O_NEGATIVE)
    }


    object AB_POSITIVE: Platelets{
        override val type: BloodGroups = BloodGroups.AB_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_POSITIVE, BloodGroups.A_POSITIVE, BloodGroups.B_POSITIVE, BloodGroups.O_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
    }

    object AB_NEGATIVE: Platelets{
        override val type: BloodGroups = BloodGroups.AB_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_POSITIVE, BloodGroups.B_NEGATIVE, BloodGroups.O_POSITIVE,BloodGroups.O_NEGATIVE, BloodGroups.A_POSITIVE, BloodGroups.A_NEGATIVE , BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_NEGATIVE)
    }


}

interface Platelets{
    val type: BloodGroups
    val canDonateTo: List<BloodGroups>
    val canReceiveFrom: List<BloodGroups>
}