package com.pratyaksh.healthykingdom.data.repositories.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.data.dto.PublicUserDto
import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.utils.Constants
import kotlinx.coroutines.tasks.await

class PublicUserFbRepoImpl(
    val fireStore: FirebaseFirestore
): RemotePublicUserFbRepo {

    override suspend fun updateUser( user: PublicUserDto){
        fireStore.collection(Constants.Collections.PUBLIC_USERS)
            .document(user.userId!!)
            .set(user)
            .await()
    }

    override suspend fun getAllUsers(): List<PublicUserDto> {
        return fireStore.collection(Constants.Collections.PUBLIC_USERS)
            .get()
            .await()
            .toObjects(PublicUserDto::class.java)
    }

    override suspend fun getUserWithId(userId: String): PublicUserDto? {
        return fireStore.collection(Constants.Collections.PUBLIC_USERS)
            .document(userId)
            .get()
            .await()
            .toObject(PublicUserDto::class.java)
    }

    override suspend fun getUserWithPhone(phone: String): PublicUserDto? {
        return fireStore.collection(Constants.Collections.PUBLIC_USERS)
            .get()
            .await()
            .toObjects(PublicUserDto::class.java)
            .find {
                it.phone == phone
            }
    }

    override suspend fun getUsersWhoProvideLoc(): List<PublicUserDto> {
        return fireStore.collection(Constants.Collections.PUBLIC_USERS)
            .get()
            .await()
            .toObjects(PublicUserDto::class.java)
            .filter {
                it.providesLocation == true
            }
    }

    override suspend fun addUser(userDto: PublicUserDto): Boolean {
        try {
            fireStore.collection(Constants.Collections.PUBLIC_USERS)
                .document(userDto.userId!!).set(userDto)
                .await()
            return true
        }catch(e: Exception){
            throw e
        }
    }

    override suspend fun updatePassword(userId: String, newPass: String) {
        fireStore.collection(Constants.Collections.PUBLIC_USERS)
            .document(userId)
            .update(Constants.UserDocField.password, newPass)
            .await()
    }
    override suspend fun deleteUser(userId: String) {
        fireStore.collection(Constants.Collections.PUBLIC_USERS)
            .document(userId).delete().await()
    }
}