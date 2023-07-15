package com.pratyaksh.healthykingdom.domain.model

import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo

data class Requests(

    val hospitalId: String,
    val blood: List<BloodGroupsInfo>,
    val plasma: MutableList<PlasmaGroupInfo>,
    val platelets: List<BloodGroupsInfo>

)
