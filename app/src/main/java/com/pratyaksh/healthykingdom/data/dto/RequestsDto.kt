package com.pratyaksh.healthykingdom.data.dto

import com.pratyaksh.healthykingdom.data.dto.request_dtos.BloodReqDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.PlasmaReqDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.PlateletsReqDto
import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo

data class RequestsDto (

    val hospitalId: String? = null,
    val bloods: BloodReqDto = BloodReqDto(),
    val plasma: PlasmaReqDto = PlasmaReqDto(),
    val platelets: PlateletsReqDto = PlateletsReqDto()

)

fun RequestsDto.toRequests(): Requests {

    val bloodReq = mutableListOf<BloodGroupsInfo>()
    val plateletsReq = mutableListOf<BloodGroupsInfo>()
    val plasmaReq = mutableListOf<PlasmaGroupInfo>()

    bloods.run {
        if(a_pos) bloodReq.add(BloodGroupsInfo.A_POSITIVE)
        if(a_neg) bloodReq.add(BloodGroupsInfo.A_NEGATIVE)
        if(b_pos) bloodReq.add(BloodGroupsInfo.B_POSITIVE)
        if(b_neg) bloodReq.add(BloodGroupsInfo.B_NEGATIVE)
        if(ab_pos) bloodReq.add(BloodGroupsInfo.AB_POSITIVE)
        if(ab_neg) bloodReq.add(BloodGroupsInfo.AB_NEGATIVE)
        if(o_pos) bloodReq.add(BloodGroupsInfo.O_POSITIVE)
        if(o_neg) bloodReq.add(BloodGroupsInfo.O_NEGATIVE)
    }

    platelets.run {
        if(a_pos) plateletsReq.add(BloodGroupsInfo.A_POSITIVE)
        if(a_neg) plateletsReq.add(BloodGroupsInfo.A_NEGATIVE)
        if(b_pos) plateletsReq.add(BloodGroupsInfo.B_POSITIVE)
        if(b_neg) plateletsReq.add(BloodGroupsInfo.B_NEGATIVE)
        if(ab_pos) plateletsReq.add(BloodGroupsInfo.AB_POSITIVE)
        if(ab_neg) plateletsReq.add(BloodGroupsInfo.AB_NEGATIVE)
        if(o_pos) plateletsReq.add(BloodGroupsInfo.O_POSITIVE)
        if(o_neg) plateletsReq.add(BloodGroupsInfo.O_NEGATIVE)
    }

    plasma.run {
        if(a_group) plasmaReq.add(PlasmaGroupInfo.Plasma_A)
        if(b_group) plasmaReq.add(PlasmaGroupInfo.Plasma_B)
        if(ab_group) plasmaReq.add(PlasmaGroupInfo.Plasma_AB)
        if(o_group) plasmaReq.add(PlasmaGroupInfo.Plasma_O)
    }


    return Requests(
        blood = bloodReq,
        platelets = plateletsReq,
        plasma = plasmaReq,
        hospitalId = hospitalId!!
    )
}