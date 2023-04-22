package com.pratyaksh.healthykingdom.ui.hospital_registration

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class RegisterHospitalVM @Inject constructor(
    val otpValidationUseCase: OtpSignInUseCase,
    val otpSendUseCase: OtpSendUseCase,
): ViewModel() {

    var uiState by mutableStateOf( RegistrationScreenUiState() )
        private set

    fun initScreenState(
        state: RegistrationScreenUiState
    ){
        uiState = state
    }

    fun toggleLocationChooser(setVisible: Boolean){
        uiState = uiState.copy(
            showLocationChooser = setVisible
        )

        Log.d("VMLogs", "UIstate: $uiState")
    }

    fun saveResendTokenAndVerId(resendToken: PhoneAuthProvider.ForceResendingToken, verId: String){
        uiState = uiState.copy(
            resendToken = resendToken,
            verificationId = verId
        )
    }

    fun setToValidationStep(){
//        isValidationStep.value = true

        uiState = uiState.copy(
            isValidationStep = true
        )

    }


    fun onCodeValueChange(newValue: String){
//        textFieldState.code.value = newValue

        uiState = uiState.copy(
            code = newValue
        )

    }

    fun onPhoneValueChange(newValue: String){
//        textFieldState.phone.value = newValue

        uiState = uiState.copy(
            phone = newValue
        )

    }

    fun onNameValueChange(newValue: String){
//        textFieldState.name.value = newValue


        uiState = uiState.copy(
            name = newValue
        )


    }


    fun onMailValueChange(newValue: String){
//        textFieldState.mail.value = newValue

        uiState = uiState.copy(
            mail = newValue
        )

    }

    fun onLocationValueChange(newValue: GeoPoint){
        uiState = uiState.copy(
            location = newValue
        )
    }

}