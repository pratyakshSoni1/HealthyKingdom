package com.pratyaksh.healthykingdom.domain.use_case.getHospital

import com.pratyaksh.healthykingdom.data.dto.toHospital
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import javax.inject.Inject

class GetHospitalByIdUseCase @Inject constructor(
    private val fbRepo: RemoteHospitalFbRepo
) {

    suspend operator fun invoke(id: String): Hospital?{
        return fbRepo.getHospitalById(id)?.toHospital()
    }

}