package com.pratyaksh.healthykingdom.domain.repository

import com.pratyaksh.healthykingdom.data.dto.AvailFluidsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto
import com.pratyaksh.healthykingdom.utils.LifeFluids

interface RemoteLifeFluidsFbRepo {

    suspend fun getLifeFluidFromHospital(hospitalId: String): AvailFluidsDto?

    suspend fun addHospitalLifeFluidData(hospitalId: String, lifeFluids: AvailFluidsDto): Boolean

    suspend fun updateFluidByHospital(hospitalId: String, lifeFluids: AvailFluidsDto, lifeFluidToBeUpdated: LifeFluids): Boolean

    suspend fun updateHospitalsBloodData(hospitalId: String, bloodData: AvailBloodDto): Boolean

    suspend fun updateHospitalsPlasmaData(hospitalId: String, plasmaData: AvailPlasmaDto): Boolean

    suspend fun updateHospitalsPlateletsData(hospitalId: String, plateletsData: AvailPlateletsDto): Boolean

    suspend fun deleteHospitalLifeFluidData(hospitalId: String): Boolean

}