package com.pratyaksh.healthykingdom.domain.use_case.add_hospital

import com.pratyaksh.healthykingdom.data.dto.AvailFluidsDto
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.data.dto.RequestsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.BloodReqDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.PlasmaReqDto
import com.pratyaksh.healthykingdom.data.dto.request_dtos.PlateletsReqDto
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.toHospitalDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddHospitalUseCase @Inject constructor(
    private val firebaseHospitalRepo: RemoteHospitalFbRepo,
    private val fbRequestsRepo: RemoteRequestsRepo,
    private val fbLifeFluidsRepo: RemoteLifeFluidsFbRepo
) {

    suspend operator fun invoke(
        hospital: Users.Hospital
    ): Flow<Resource<Boolean>> = flow{
        try {
            val task1 = firebaseHospitalRepo.addHospital(hospital.toHospitalDto())

            val task2 = fbLifeFluidsRepo.addHospitalLifeFluidData(
                hospital.userId,
                AvailFluidsDto(
                    AvailBloodDto(),
                    AvailPlasmaDto(),
                    AvailPlateletsDto()
                )
            )

            val task3 = fbRequestsRepo.addRequest(
                RequestsDto(
                    hospitalId = hospital.userId,
                    BloodReqDto(),
                    PlasmaReqDto(),
                    PlateletsReqDto()
                )
            )

            task1.await()
            task2.await()
            task3.await()

            if(task1.isSuccessful && task2.isSuccessful && task3.isSuccessful)
                emit(Resource.Success(true))
            else
                emit(Resource.Error("User can't be added, try again later"))
        }catch(e: Exception){
            emit(Resource.Error("Unexpected error occured"))
        }
    }

}