package com.pratyaksh.healthykingdom.utils

sealed class PlateletsGroupInfo(
    val type: BloodGroups,
    val canDonateTo: List<BloodGroups>,
    val canReceiveFrom: List<BloodGroups>
) {

    object A_POSITIVE : PlateletsGroupInfo(
        type = BloodGroups.A_POSITIVE,
        canDonateTo = listOf<BloodGroups>(BloodGroups.A_POSITIVE, BloodGroups.O_POSITIVE),
        canReceiveFrom = listOf<BloodGroups>(
            BloodGroups.A_POSITIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE
        ),
    )

    object A_NEGATIVE : PlateletsGroupInfo(
        type = BloodGroups.A_NEGATIVE,
        canDonateTo = listOf<BloodGroups>(
            BloodGroups.O_POSITIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.O_NEGATIVE
        ),
        canReceiveFrom = listOf<BloodGroups>(BloodGroups.A_NEGATIVE, BloodGroups.AB_NEGATIVE),
    )


    object B_POSITIVE : PlateletsGroupInfo(
        type = BloodGroups.B_POSITIVE,
        canDonateTo = listOf<BloodGroups>(BloodGroups.O_POSITIVE),
        canReceiveFrom = listOf<BloodGroups>(
            BloodGroups.B_POSITIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE
        ),
    )

    object B_NEGATIVE : PlateletsGroupInfo(
        type = BloodGroups.B_NEGATIVE,
        canDonateTo = listOf<BloodGroups>(
            BloodGroups.B_POSITIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE
        ),
        canReceiveFrom = listOf<BloodGroups>(
            BloodGroups.B_NEGATIVE,
            BloodGroups.B_POSITIVE,
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE
        ),
    )


    object O_POSITIVE : PlateletsGroupInfo(
        type = BloodGroups.O_POSITIVE,
        canDonateTo = listOf<BloodGroups>(BloodGroups.O_POSITIVE),
        canReceiveFrom = listOf<BloodGroups>(
            BloodGroups.B_POSITIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.O_POSITIVE,
            BloodGroups.O_NEGATIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE
        ),
    )

    object O_NEGATIVE : PlateletsGroupInfo(
        type = BloodGroups.O_NEGATIVE,
        canDonateTo = listOf<BloodGroups>(BloodGroups.O_POSITIVE, BloodGroups.O_NEGATIVE),
        canReceiveFrom = listOf<BloodGroups>(
            BloodGroups.A_NEGATIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.AB_NEGATIVE,
            BloodGroups.O_NEGATIVE
        ),
    )


    object AB_POSITIVE : PlateletsGroupInfo(
        type = BloodGroups.AB_POSITIVE,
        canDonateTo = listOf<BloodGroups>(
            BloodGroups.AB_POSITIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.B_POSITIVE,
            BloodGroups.O_POSITIVE
        ),
        canReceiveFrom = listOf<BloodGroups>(BloodGroups.AB_POSITIVE, BloodGroups.AB_NEGATIVE),
    )

    object AB_NEGATIVE : PlateletsGroupInfo(
        type = BloodGroups.AB_NEGATIVE,
        canDonateTo = listOf<BloodGroups>(
            BloodGroups.B_POSITIVE,
            BloodGroups.B_NEGATIVE,
            BloodGroups.O_POSITIVE,
            BloodGroups.O_NEGATIVE,
            BloodGroups.A_POSITIVE,
            BloodGroups.A_NEGATIVE,
            BloodGroups.AB_POSITIVE,
            BloodGroups.AB_NEGATIVE
        ),
        canReceiveFrom = listOf<BloodGroups>(BloodGroups.AB_NEGATIVE),
    )


}

fun List<PlateletsGroupInfo>.getOnlyGroup(): List<BloodGroups> {
    return this.map {
        when (it) {

            PlateletsGroupInfo.A_POSITIVE -> BloodGroups.A_POSITIVE
            PlateletsGroupInfo.A_NEGATIVE -> BloodGroups.A_NEGATIVE
            PlateletsGroupInfo.AB_POSITIVE -> BloodGroups.AB_POSITIVE
            PlateletsGroupInfo.AB_NEGATIVE -> BloodGroups.AB_NEGATIVE
            PlateletsGroupInfo.O_POSITIVE -> BloodGroups.O_POSITIVE
            PlateletsGroupInfo.O_NEGATIVE -> BloodGroups.O_NEGATIVE
            PlateletsGroupInfo.B_POSITIVE -> BloodGroups.B_POSITIVE
            PlateletsGroupInfo.B_NEGATIVE -> BloodGroups.B_NEGATIVE

        }
    }
}

