package com.pratyaksh.healthykingdom.ui.hospital_details

import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

data class HospitalDetailsUiState (

    val isLoading: Boolean= false,
    val isError: Boolean= false,
    val hospital: Users.Hospital? = null,
    val bloods: AvailBlood = AvailBlood(0,0,0,0,0,0,0,0),
    val plasma: AvailPlasma = AvailPlasma(0,0,0,0),
    val platelets: AvailPlatelets = AvailPlatelets(0,0,0,0,0,0,0,0),
    val showFluidDialog: Boolean = false,
    val dialogFluidType: LifeFluids? = null,
    val dialogBloodGroup: BloodGroupsInfo? = null,
    val dialogPlateletsGroup: PlateletsGroupInfo? = null,
    val dialogPlasmaGroup: PlasmaGroupInfo? = null

)