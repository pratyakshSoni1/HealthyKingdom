package com.pratyaksh.healthykingdom.domain.model

import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.Plasma

data class Requests(

    val hospitalId: String,
    val blood: List<BloodGroups>,
    val plasma: List<Plasma>,
    val platelets: List<BloodGroups>

)
