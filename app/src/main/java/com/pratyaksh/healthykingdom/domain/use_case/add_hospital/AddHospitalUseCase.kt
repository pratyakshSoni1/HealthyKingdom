package com.pratyaksh.healthykingdom.domain.use_case.add_hospital

import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteFirebaseRepo
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class AddHospitalUseCase @Inject constructor(
    private val firebaseHospitalRepo: RemoteFirebaseRepo
) {

    suspend operator fun invoke(
        hospitalsDto: HospitalsDto
    ): Flow<Resource<Boolean>> = firebaseHospitalRepo.addHospital(hospitalsDto)

}