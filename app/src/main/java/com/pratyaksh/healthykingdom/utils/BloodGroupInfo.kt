package com.pratyaksh.healthykingdom.utils

sealed class BloodGroupsInfo {

    object A_POSITIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.A_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_POSITIVE, BloodGroups.AB_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_POSITIVE, BloodGroups.A_NEGATIVE, BloodGroups.O_POSITIVE, BloodGroups.O_NEGATIVE)
    }

    object A_NEGATIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.A_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_NEGATIVE, BloodGroups.A_POSITIVE, BloodGroups.AB_NEGATIVE, BloodGroups.AB_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.A_NEGATIVE, BloodGroups.O_NEGATIVE)
    }


    object B_POSITIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.B_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_POSITIVE, BloodGroups.B_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_POSITIVE, BloodGroups.B_NEGATIVE, BloodGroups.O_POSITIVE, BloodGroups.O_NEGATIVE)
    }

    object B_NEGATIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.B_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_POSITIVE, BloodGroups.B_NEGATIVE, BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_NEGATIVE, BloodGroups.O_NEGATIVE)
    }


    object O_POSITIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.O_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.B_POSITIVE, BloodGroups.O_POSITIVE, BloodGroups.A_POSITIVE, BloodGroups.AB_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.O_NEGATIVE, BloodGroups.O_POSITIVE)
    }

    object O_NEGATIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.O_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>( BloodGroups.O_POSITIVE, BloodGroups.O_NEGATIVE, BloodGroups.B_NEGATIVE, BloodGroups.B_POSITIVE, BloodGroups.A_NEGATIVE, BloodGroups.A_POSITIVE, BloodGroups.AB_NEGATIVE, BloodGroups.AB_POSITIVE )
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.O_NEGATIVE)
    }


    object AB_POSITIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.AB_POSITIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_POSITIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE, BloodGroups.A_POSITIVE, BloodGroups.A_NEGATIVE, BloodGroups.B_POSITIVE, BloodGroups.B_NEGATIVE, BloodGroups.O_POSITIVE, BloodGroups.O_NEGATIVE)
    }

    object AB_NEGATIVE: BloodGroupInterface{
        override val type: BloodGroups = BloodGroups.AB_NEGATIVE
        override val canDonateTo: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE)
        override val canReceiveFrom: List<BloodGroups> = listOf<BloodGroups>(BloodGroups.AB_NEGATIVE, BloodGroups.A_NEGATIVE, BloodGroups.B_NEGATIVE, BloodGroups.O_NEGATIVE)
    }




}

interface BloodGroupInterface{
    val type: BloodGroups
    val canDonateTo: List<BloodGroups>
    val canReceiveFrom: List<BloodGroups>
}

enum class BloodGroups{
    A_POSITIVE,
    A_NEGATIVE,
    B_POSITIVE,
    B_NEGATIVE,
    O_POSITIVE,
    O_NEGATIVE,
    AB_POSITIVE,
    AB_NEGATIVE

}

fun List<BloodGroupInterface>.getOnlyGroup(): List<BloodGroups>{
    return this.map {
        when(it){

            BloodGroupsInfo.A_POSITIVE -> BloodGroups.A_POSITIVE
            BloodGroupsInfo.A_NEGATIVE -> BloodGroups.A_NEGATIVE
            BloodGroupsInfo.AB_POSITIVE -> BloodGroups.AB_POSITIVE
            BloodGroupsInfo.AB_NEGATIVE -> BloodGroups.AB_NEGATIVE
            BloodGroupsInfo.O_POSITIVE -> BloodGroups.O_POSITIVE
            BloodGroupsInfo.O_NEGATIVE -> BloodGroups.O_NEGATIVE
            BloodGroupsInfo.B_POSITIVE -> BloodGroups.B_POSITIVE
            BloodGroupsInfo.B_NEGATIVE -> BloodGroups.B_NEGATIVE
            else -> BloodGroups.O_NEGATIVE

        }
    }
}