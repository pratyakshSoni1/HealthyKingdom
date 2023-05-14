package com.pratyaksh.healthykingdom.domain.use_case.update_lifefluids

import com.pratyaksh.healthykingdom.domain.model.AvailFluids
import com.pratyaksh.healthykingdom.domain.model.toAvailFluidsDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateLifeFluidsUseCase @Inject constructor(
    private val lifeFluidsRepoImpl: RemoteLifeFluidsFbRepo
) {

    operator fun invoke(
        fluidTypeToUpdate: LifeFluids,
        fluidData: AvailFluids,
        hospitalId: String
    )=flow<Resource<Boolean>>{
        try{
            val task = lifeFluidsRepoImpl.updateFluidByHospital(
                hospitalId,
                lifeFluids = fluidData.toAvailFluidsDto(),
                lifeFluidToBeUpdated = fluidTypeToUpdate
            )
            if(task) emit(Resource.Success(true)) else emit(Resource.Error("Fluids updation failed"))
        }catch(e: Exception){
            emit(Resource.Error("Unexpected error, tru later !"))
        }
    }

}