package com.pratyaksh.healthykingdom.data.repositories.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.data.dto.AvailFluidsDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailBloodDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlasmaDto
import com.pratyaksh.healthykingdom.data.dto.lifefluids.AvailPlateletsDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.LifeFluids
import kotlinx.coroutines.tasks.await

class LifeFluidsRepoImpl(
    private val fireStore: FirebaseFirestore
): RemoteLifeFluidsFbRepo {
    override suspend fun getLifeFluidFromHospital(hospitalId: String): AvailFluidsDto? {
        try{
            return fireStore.collection(Constants.Collections.LIFE_FLUIDS)
                .document(hospitalId)
                .get().await().toObject(AvailFluidsDto::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun addHospitalLifeFluidData(
        hospitalId: String,
        lifeFluids: AvailFluidsDto
    ): Boolean {
        try{
            fireStore.collection(Constants.Collections.LIFE_FLUIDS)
                .document(hospitalId).set(lifeFluids).await()
            return true
        }catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun updateFluidByHospital(
        hospitalId: String,
        lifeFluids: AvailFluidsDto,
        lifeFluidToBeUpdated: LifeFluids
    ): Boolean {

            val fbDocRef = fireStore.collection(Constants.Collections.LIFE_FLUIDS)
                .document(hospitalId)

            val task = when(lifeFluidToBeUpdated){
                LifeFluids.PLASMA -> fbDocRef.update( Constants.LifeFluidFieldNames.plasma, lifeFluids.plasma )
                LifeFluids.BLOOD -> fbDocRef.update( Constants.LifeFluidFieldNames.blood, lifeFluids.bloods )
                LifeFluids.PLATELETS -> fbDocRef.update( Constants.LifeFluidFieldNames.platelets, lifeFluids.platelets )
            }
            task.await()

        return task.isSuccessful

    }

    override suspend fun updateHospitalsBloodData(
        hospitalId: String,
        bloodData: AvailBloodDto
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateHospitalsPlasmaData(
        hospitalId: String,
        plasmaData: AvailPlasmaDto
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateHospitalsPlateletsData(
        hospitalId: String,
        plateletsData: AvailPlateletsDto
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHospitalLifeFluidData(hospitalId: String): Boolean {
        try{
            fireStore.collection(Constants.Collections.LIFE_FLUIDS)
                .document(hospitalId).delete().await()
            return true
        }catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }
}