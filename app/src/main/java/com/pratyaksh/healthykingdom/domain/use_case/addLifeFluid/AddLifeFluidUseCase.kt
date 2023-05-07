package com.pratyaksh.healthykingdom.domain.use_case.addLifeFluid

import com.pratyaksh.healthykingdom.data.dto.AvailFluidsDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddLifeFluidUseCase @Inject constructor(
    val fluidsRepo: RemoteLifeFluidsFbRepo
) {

    operator fun invoke(
        hospitalId: String,
        fluidsData: AvailFluidsDto
    ) = flow<Resource<Boolean>>{
        try{
            emit(Resource.Loading("Fetching data..."))
            fluidsRepo.addHospitalLifeFluidData(hospitalId, fluidsData)
            emit(Resource.Success(true))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Unexpected eror occured !"))
        }

    }

}