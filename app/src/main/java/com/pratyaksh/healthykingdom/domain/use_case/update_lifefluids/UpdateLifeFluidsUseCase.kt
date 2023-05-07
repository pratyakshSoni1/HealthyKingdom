package com.pratyaksh.healthykingdom.domain.use_case.update_lifefluids

import com.pratyaksh.healthykingdom.domain.model.AvailFluids
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.utils.LifeFluids

class UpdateLifeFluidsUseCase(
    lifeFluidsRepoImpl: RemoteLifeFluidsFbRepo
) {

    operator fun invoke(
        fluidTypeToUpdate: LifeFluids,
        fluidData: AvailFluids
    ){

        try{

        }catch(e: Exception){

        }

    }

}