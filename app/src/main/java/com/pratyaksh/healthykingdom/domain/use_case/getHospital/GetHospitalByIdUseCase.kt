package com.pratyaksh.healthykingdom.domain.use_case.getHospital

import com.pratyaksh.healthykingdom.data.dto.toHospital
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import javax.inject.Inject

class GetHospitalByIdUseCase @Inject constructor(
    private val fbRepo: RemoteHospitalFbRepo
) {

    suspend operator fun invoke(id: String): Users.Hospital?{
        return fbRepo.getHospitalById(id)?.toHospital()
    }

}