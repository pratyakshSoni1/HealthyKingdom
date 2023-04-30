package com.pratyaksh.healthykingdom.ui.hospital_registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class RegisterHospitalVM @Inject constructor(
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

    fun onPhoneValueChange(newValue: String){
        uiState = uiState.copy(
            phone = newValue
        )
    }

    fun onNameValueChange(newValue: String){
        uiState = uiState.copy(
            name = newValue
        )
    }


    fun onMailValueChange(newValue: String) {
        uiState = uiState.copy(
            mail = newValue
        )
    }

    fun onPassValueChange(newValue: String){
        uiState = uiState.copy(
            password = newValue
        )
    }

    fun onConfirmPassValueChange(newValue: String){
        uiState = uiState.copy(
            confirmPassword = newValue
        )
    }
    fun onErrorTxtChage(newValue: String){
        uiState = uiState.copy(
            errorText = newValue
        )
    }

    fun toggleErrorDialog(
        setToVisible: Boolean, text: String = "Something went wrong, try later"
    ){
        uiState = uiState.copy(
            errorText = text,
            showError = setToVisible,
            isLoading = false,
            showLocationChooser = false
        )
    }

    fun toggleLoadingScr(setToVisible: Boolean?){
        uiState = uiState.copy(
            isLoading = setToVisible ?: !uiState.isLoading
        )
    }

    fun onLocationValueChange(newValue: GeoPoint){
        uiState = uiState.copy(
            location = newValue
        )
    }



}