package com.pratyaksh.healthykingdom.domain.use_case.getFluidsData

import com.pratyaksh.healthykingdom.data.dto.AvailFluidsDto
import com.pratyaksh.healthykingdom.data.dto.toAvailFluids
import com.pratyaksh.healthykingdom.domain.model.AvailFluids
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFluidsByHospitalUseCase @Inject constructor(
    private val fluidsRepo: RemoteLifeFluidsFbRepo
) {

    operator fun invoke(
        hospitalId: String
    ) = flow<Resource<AvailFluids?>>{
        try{
            emit(Resource.Loading("Fetching data..."))
            fluidsRepo.getLifeFluidFromHospital(hospitalId)?.let {
                emit(Resource.Success(it.toAvailFluids()))
            } ?: emit(Resource.Error("Unexpected eror occured !"))

        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Unexpected eror occured !"))
        }
    }

}