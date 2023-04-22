package com.pratyaksh.healthykingdom.ui.hospital_registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel()
class OtpValidationVM @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(OtpValidationUiState())
        private set

    fun onCodeChange(newVal: String){
        uiState = uiState.copy(
            code = newVal
        )
    }

    fun initScreen(verificationId: String, phoneNum: String, resendingToken: ForceResendingToken){
        uiState = uiState.copy(
            verificationId = verificationId,
            phone = phoneNum,
            resendToken= resendingToken
        )
    }


}