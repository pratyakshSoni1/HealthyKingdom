package com.pratyaksh.healthykingdom.ui.profile_screen

import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Gender
import org.osmdroid.util.GeoPoint


data class ProfileScreenUiState (
    val currentUserId: String? = null,
    val location: GeoPoint? = null,
    val showLocationChooser: Boolean = false,
    val showError: Boolean = false,
    val errorText: String= "",
    val isLoading: Boolean = false,
    val isEditingMode: Boolean = false,
    val accountType: AccountTypes? = null,
    val isUpdateBtnActive: Boolean = false,
    val onErrorCloseAction:()->Unit = { Unit },


    val name: String = "",
    val phone: String = "",
    val mail: String = "",
    val age: String = "",
    val vehicleNumber: String = "",
    val gender: Gender= Gender.FEMALE,
    val providesLocation: Boolean = false
)