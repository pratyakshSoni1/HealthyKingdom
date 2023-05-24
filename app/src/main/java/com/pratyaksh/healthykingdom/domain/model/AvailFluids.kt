package com.pratyaksh.healthykingdom.domain.model

import com.pratyaksh.healthykingdom.data.dto.AvailFluidsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toBloodModel
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toPlasmaModel
import com.pratyaksh.healthykingdom.data.dto.lifefluids.toPlateletsModel
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toBloodDto
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toPlasmaDto
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toPlateletsDto
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

data class AvailFluids (

    val bloods: AvailBlood,
    val plasma: AvailPlasma,
    val platelets: AvailPlatelets,

    )


fun AvailFluids.toAvailFluidsDto(): AvailFluidsDto{
    return AvailFluidsDto(
        bloods.toBloodDto(),
        plasma.toPlasmaDto(),
        platelets.toPlateletsDto()
    )
}

fun AvailBlood.getAvailGroups(): List<BloodGroupsInfo>{

    val availBloods= mutableListOf<BloodGroupsInfo>()
    if( aPos > 0 )
        availBloods.add(BloodGroupsInfo.A_POSITIVE)
    if( aNeg > 0 )
        availBloods.add(BloodGroupsInfo.A_NEGATIVE)
    if( bPos > 0 )
        availBloods.add(BloodGroupsInfo.B_POSITIVE)
    if( bNeg > 0 )
        availBloods.add(BloodGroupsInfo.B_NEGATIVE)
    if( abPos > 0 )
        availBloods.add(BloodGroupsInfo.AB_POSITIVE)
    if( abNeg > 0 )
        availBloods.add(BloodGroupsInfo.AB_NEGATIVE)
    if( oPos > 0 )
        availBloods.add(BloodGroupsInfo.O_POSITIVE)
    if( oNeg > 0 )
        availBloods.add(BloodGroupsInfo.O_NEGATIVE)

    return availBloods
}

fun AvailPlatelets.getAvailGroups(): List<PlateletsGroupInfo>{

    val availPlatelets= mutableListOf<PlateletsGroupInfo>()
    if( aPos > 0 )
        availPlatelets.add(PlateletsGroupInfo.A_POSITIVE)
    if( aNeg > 0 )
        availPlatelets.add(PlateletsGroupInfo.A_NEGATIVE)
    if( bPos > 0 )
        availPlatelets.add(PlateletsGroupInfo.B_POSITIVE)
    if( bNeg > 0 )
        availPlatelets.add(PlateletsGroupInfo.B_NEGATIVE)
    if( abPos > 0 )
        availPlatelets.add(PlateletsGroupInfo.AB_POSITIVE)
    if( abNeg > 0 )
        availPlatelets.add(PlateletsGroupInfo.AB_NEGATIVE)
    if( oPos > 0 )
        availPlatelets.add(PlateletsGroupInfo.O_POSITIVE)
    if( oNeg > 0 )
        availPlatelets.add(PlateletsGroupInfo.O_NEGATIVE)

    return availPlatelets
}

fun AvailPlasma.getAvailGroups(): List<PlasmaGroupInfo>{

    val availPlasma= mutableListOf<PlasmaGroupInfo>()
    if( aGroup > 0 )
        availPlasma.add(PlasmaGroupInfo.Plasma_A)
    if( bGroup > 0 )
        availPlasma.add(PlasmaGroupInfo.Plasma_B)
    if( abGroup > 0 )
        availPlasma.add(PlasmaGroupInfo.Plasma_AB)
    if( oGroup > 0 )
        availPlasma.add(PlasmaGroupInfo.Plasma_O)

    return availPlasma
}

fun AvailBlood.getQuantityMap(): Map<BloodGroupsInfo, Int>{
    val respMap = mutableMapOf<BloodGroupsInfo, Int>()

    respMap[BloodGroupsInfo.A_POSITIVE] = aPos
    respMap[BloodGroupsInfo.B_POSITIVE] = aPos
    respMap[BloodGroupsInfo.AB_POSITIVE] = aPos
    respMap[BloodGroupsInfo.O_POSITIVE] = aPos
    respMap[BloodGroupsInfo.A_NEGATIVE] = aNeg
    respMap[BloodGroupsInfo.B_NEGATIVE] = bNeg
    respMap[BloodGroupsInfo.AB_NEGATIVE] = abNeg
    respMap[BloodGroupsInfo.O_NEGATIVE] = oNeg


    return respMap
}
fun AvailPlatelets.getQuantityMap(): Map<PlateletsGroupInfo, Int>{
    val respMap = mutableMapOf<PlateletsGroupInfo, Int>()

    respMap[PlateletsGroupInfo.A_POSITIVE] = aPos
    respMap[PlateletsGroupInfo.B_POSITIVE] = aPos
    respMap[PlateletsGroupInfo.AB_POSITIVE] = aPos
    respMap[PlateletsGroupInfo.O_POSITIVE] = aPos
    respMap[PlateletsGroupInfo.A_NEGATIVE] = aNeg
    respMap[PlateletsGroupInfo.B_NEGATIVE] = bNeg
    respMap[PlateletsGroupInfo.AB_NEGATIVE] = abNeg
    respMap[PlateletsGroupInfo.O_NEGATIVE] = oNeg


    return respMap
}

fun AvailPlasma.getQuantityMap(): Map<PlasmaGroupInfo, Int>{
    val respMap = mutableMapOf<PlasmaGroupInfo, Int>()

    respMap[PlasmaGroupInfo.Plasma_A] = aGroup
    respMap[PlasmaGroupInfo.Plasma_B] = bGroup
    respMap[PlasmaGroupInfo.Plasma_AB] = abGroup
    respMap[PlasmaGroupInfo.Plasma_O] = oGroup


    return respMap
}
