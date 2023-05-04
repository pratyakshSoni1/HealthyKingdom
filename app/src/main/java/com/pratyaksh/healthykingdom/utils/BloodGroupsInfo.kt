package com.pratyaksh.healthykingdom.utils

sealed class BloodGroupsInfo(
    val type: BloodGroups, val canDonateTo: List<BloodGroups>, val canReceiveFrom: List<BloodGroups>
) {

    object A_POSITIVE : BloodGroupsInfo(
        type = BloodGroups.A_POSITIVE,
        canDonateTo = listOf(BloodGroups.A_POSITIVE, BloodGroups.AB_POSITIVE),
        canReceiveFrom = listOf(
            BloodGroups.A_POSITIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.O_POSITIVE,
            BloodGroups.O_NEGATIVE
        )
    )

    object A_NEGATIVE : BloodGroupsInfo(
        type = BloodGroups.A_NEGATIVE,
        canDonateTo = listOf(
            BloodGroups.A_NEGATIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.AB_NEGATIVE,
            BloodGroups.AB_POSITIVE
        ),
        canReceiveFrom = listOf(BloodGroups.A_NEGATIVE, BloodGroups.O_NEGATIVE)
    )

    object B_POSITIVE : BloodGroupsInfo(
        type = BloodGroups.B_POSITIVE,
        canDonateTo = listOf(BloodGroups.AB_POSITIVE, BloodGroups.B_POSITIVE),
        canReceiveFrom = listOf(
            BloodGroups.B_POSITIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.O_POSITIVE,
            BloodGroups.O_NEGATIVE
        )
    )

    object B_NEGATIVE : BloodGroupsInfo(
        type = BloodGroups.B_NEGATIVE,
        canDonateTo = listOf(
            BloodGroups.B_POSITIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE
        ),
        canReceiveFrom = listOf(BloodGroups.B_NEGATIVE, BloodGroups.O_NEGATIVE)
    )


    object O_POSITIVE : BloodGroupsInfo(
        type = BloodGroups.O_POSITIVE,
        canDonateTo = listOf(
            BloodGroups.B_POSITIVE,
            BloodGroups.O_POSITIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.AB_POSITIVE
        ),
        canReceiveFrom = listOf(BloodGroups.O_NEGATIVE, BloodGroups.O_POSITIVE)
    )

    object O_NEGATIVE : BloodGroupsInfo(
        type = BloodGroups.O_NEGATIVE,
        canDonateTo = listOf(
            BloodGroups.O_POSITIVE,
            BloodGroups.O_NEGATIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.B_POSITIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.AB_NEGATIVE,
            BloodGroups.AB_POSITIVE
        ),
        canReceiveFrom = listOf(BloodGroups.O_NEGATIVE)
    )


    object AB_POSITIVE : BloodGroupsInfo(
        type = BloodGroups.AB_POSITIVE,
        canDonateTo = listOf(BloodGroups.AB_POSITIVE),
        canReceiveFrom = listOf(
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.B_POSITIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.O_POSITIVE,
            BloodGroups.O_NEGATIVE
        )
    )

    object AB_NEGATIVE : BloodGroupsInfo(
        type = BloodGroups.AB_NEGATIVE,
        canDonateTo = listOf(BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE),
        canReceiveFrom = listOf(
            BloodGroups.AB_NEGATIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.O_NEGATIVE
        )
    )

}

enum class BloodGroups {
    A_POSITIVE,
    A_NEGATIVE,
    B_POSITIVE,
    B_NEGATIVE,
    O_POSITIVE,
    O_NEGATIVE,
    AB_POSITIVE,
    AB_NEGATIVE,
    ERROR_TYPE

}

fun List<BloodGroupsInfo>.getOnlyGroup(): List<BloodGroups> {
    return this.map { it.type }
}