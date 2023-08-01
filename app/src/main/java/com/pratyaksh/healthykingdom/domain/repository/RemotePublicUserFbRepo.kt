package com.pratyaksh.healthykingdom.domain.repository

import com.pratyaksh.healthykingdom.data.dto.PublicUserDto

interface RemotePublicUserFbRepo {

    suspend fun getAllUsers(): List<PublicUserDto>

    suspend fun getUserWithId(userId: String): PublicUserDto?

    suspend fun getUserWithPhone(phone: String): PublicUserDto?

    suspend fun getUsersWhoProvideLoc(): List<PublicUserDto>

    suspend fun addUser(userDto: PublicUserDto): Boolean
    suspend fun updateUser(userDto: PublicUserDto)
    suspend fun deleteUser(userId: String)

}