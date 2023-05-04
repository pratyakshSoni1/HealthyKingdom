package com.pratyaksh.healthykingdom.ui.hospital_registration

import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Gender
import org.osmdroid.util.GeoPoint


data class RegistrationScreenUiState (
    val location: GeoPoint? = null,
    val isValidationStep: Boolean = false,
    val verificationId: String? = null,
    val resendToken: PhoneAuthProvider.ForceResendingToken? = null,
    val showLocationChooser: Boolean = false,
    val showError: Boolean = false,
    val errorText: String= "",
    val isLoading: Boolean = false,
    val accountType: AccountTypes = AccountTypes.PUBLIC_USER,
    val isAccMenuExpanded: Boolean = false,


    val name: String = "",
    val phone: String = "",
    val code: String = "",
    val mail: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val age: String = "",
    val vehicleNumber: String = "",
    val gender: Gender= Gender.FEMALE,
    val providesLocation: Boolean = false
)

data class OtpValidationUiState(
    val code: String= "",
    val isSent: Boolean = true,
    val phone: String = "",
    val isResendAvail: Boolean = false,
    val verificationId: String = "",
    val resendToken: PhoneAuthProvider.ForceResendingToken? = null,
    val resendTimeout: Int = 0,
    val isTimoutRunning: Boolean = false,
    val user:Users?= null,
    val showError: Boolean = false,
    val errorText: String= "",
    val isLoading: Boolean = false,

    )