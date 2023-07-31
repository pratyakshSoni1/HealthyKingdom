package com.pratyaksh.healthykingdom.domain.use_case.get_ambulance

import com.pratyaksh.healthykingdom.data.dto.toFBGeopoint
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.toAmbulanceDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

class UpdateAmbulanceLocUseCase @Inject constructor(
    private val fbRepo: RemoteAmbulanceFbRepo
) {

    operator fun invoke(userId:String, location: GeoPoint) = flow<Resource<Unit>>{
        try{
            fbRepo.updateAmbulanceLoc(userId, location.toFBGeopoint())
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            emit(Resource.Error("Can't update data"))
            e.printStackTrace()
        }

    }

}