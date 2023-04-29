package com.pratyaksh.healthykingdom.domain.repository

import com.pratyaksh.healthykingdom.data.dto.AmbulanceDto
import com.pratyaksh.healthykingdom.domain.model.Ambulance

interface RemoteAmbulanceFbRepo {

    suspend fun getAllAmbulances(): List<AmbulanceDto>
    suspend fun getAllVacant(): List<AmbulanceDto>
    suspend fun getAllOccupiedAmbulance(): List<AmbulanceDto>
    suspend fun getAllOnlineAmbulances(): List<AmbulanceDto>
    suspend fun getAllOfflineAmbulances(): List<AmbulanceDto>
    suspend fun getAmbulanceByNumber( vehicleNum: String ): AmbulanceDto?
    suspend fun addAmbulance( ambulanceDto: AmbulanceDto ): Boolean




}