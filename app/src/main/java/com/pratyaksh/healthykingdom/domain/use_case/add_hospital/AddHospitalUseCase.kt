package com.pratyaksh.healthykingdom.domain.use_case.add_hospital

import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddHospitalUseCase @Inject constructor(
    private val firebaseHospitalRepo: RemoteHospitalFbRepo
) {

    suspend operator fun invoke(
        hospitalsDto: HospitalsDto
    ): Flow<Resource<Boolean>> = firebaseHospitalRepo.addHospital(hospitalsDto)

}