package com.pratyaksh.healthykingdom.ui.otp_verification

import com.google.firebase.auth.PhoneAuthProvider

data class OtpVerificationUiState(
    val code: String= "",
    val isSent: Boolean = true,
    val phone: String = "",
    val isResendAvail: Boolean = false,
    val verificationId: String = "",
    val resendToken: PhoneAuthProvider.ForceResendingToken? = null,
    val resendTimeout: Int = 0,
    val isTimoutRunning: Boolean = false,
    val showError: Boolean = false,
    val errorText: String= "",
    val isLoading: Boolean = false,
    val onErrorCloseAction :()->Unit = { Unit }

    )