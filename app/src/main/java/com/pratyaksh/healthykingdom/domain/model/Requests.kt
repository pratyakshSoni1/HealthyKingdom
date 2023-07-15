package com.pratyaksh.healthykingdom.domain.model

import com.pratyaksh.healthykingdom.data.dto.RequestsDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.BloodReqDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.PlasmaReqDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.PlateletsReqDto
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo

data class Requests(

    val hospitalId: String,
    val blood: List<BloodGroupsInfo>,
    val plasma: List<PlasmaGroupInfo>,
    val platelets: List<BloodGroupsInfo>

)

fun Requests.toRequestsDto(): RequestsDto{

    val bloodReq = BloodReqDto(
        a_pos= blood.contains(BloodGroupsInfo.A_POSITIVE),
        a_neg= blood.contains(BloodGroupsInfo.A_NEGATIVE),
        b_pos= blood.contains(BloodGroupsInfo.B_POSITIVE),
        b_neg= blood.contains(BloodGroupsInfo.B_NEGATIVE),
        ab_pos= blood.contains(BloodGroupsInfo.AB_POSITIVE),
        ab_neg= blood.contains(BloodGroupsInfo.AB_NEGATIVE),
        o_pos= blood.contains(BloodGroupsInfo.O_POSITIVE),
        o_neg= blood.contains(BloodGroupsInfo.O_NEGATIVE),
    )

    val plateletsReq = PlateletsReqDto(
        a_pos= platelets.contains(BloodGroupsInfo.A_POSITIVE),
        a_neg= platelets.contains(BloodGroupsInfo.A_NEGATIVE),
        b_pos= platelets.contains(BloodGroupsInfo.B_POSITIVE),
        b_neg= platelets.contains(BloodGroupsInfo.B_NEGATIVE),
        ab_pos= platelets.contains(BloodGroupsInfo.AB_POSITIVE),
        ab_neg= platelets.contains(BloodGroupsInfo.AB_NEGATIVE),
        o_pos= platelets.contains(BloodGroupsInfo.O_POSITIVE),
        o_neg= platelets.contains(BloodGroupsInfo.O_NEGATIVE),
    )

    val plasmaReq = PlasmaReqDto(
        a_group= plasma.contains(PlasmaGroupInfo.Plasma_A),
        b_group= plasma.contains(PlasmaGroupInfo.Plasma_B),
        ab_group= plasma.contains(PlasmaGroupInfo.Plasma_AB),
        o_group= plasma.contains(PlasmaGroupInfo.Plasma_O)
    )

    return RequestsDto(
        hospitalId,
        bloods = bloodReq,
        platelets = plateletsReq,
        plasma = plasmaReq
    )
}
