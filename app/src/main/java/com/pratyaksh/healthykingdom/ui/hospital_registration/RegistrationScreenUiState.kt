package com.pratyaksh.healthykingdom.ui.hospital_registration

import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
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

    val name: String = "",
    val phone: String = "",
    val code: String = "",
    val mail: String = "",
    val password: String = "",
    val confirmPassword: String = ""
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
    val hospitalDto:HospitalsDto?= null,
    val showError: Boolean = false,
    val errorText: String= "",
    val isLoading: Boolean = false,

    )