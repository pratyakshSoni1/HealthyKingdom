package com.pratyaksh.healthykingdom.ui.otp_verification

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.use_case.add_ambulance.AddAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.add_hospital.AddHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.add_public_user.AddPublicUserCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSignInUseCase
import com.pratyaksh.healthykingdom.domain.use_case.settings.AddSettingsUseCase
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class OtpVerificationVM @Inject constructor(
    val otpSignInUseCase: OtpSignInUseCase,
    val otpSendUseCase: OtpSendUseCase,
    private val addHospitalUseCase: AddHospitalUseCase,
    private val addPublicUseCse: AddPublicUserCase,
    private val addAmbulanceUseCase: AddAmbulanceUserCase,
    private val settingsRepo: AddSettingsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(OtpVerificationUiState())
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
        activateTimeout()
    }

    fun activateTimeout(){
        uiState = uiState.copy(resendTimeout = 0, isTimoutRunning = true, isResendAvail = false)

        viewModelScope.launch{
            while(uiState.isTimoutRunning){
                delay(1000L)
                uiState = uiState.copy(
                    resendTimeout = uiState.resendTimeout + 1
                )
                if(uiState.resendTimeout >= 60 )
                    deactivateTimeout()
            }
        }

    }

    fun deactivateTimeout(){
        uiState = uiState.copy(isTimoutRunning = false, resendTimeout = 0, isResendAvail = true)
    }

    fun updateVerificationIdAndToken(verId:String, resToken: PhoneAuthProvider.ForceResendingToken){

        uiState = uiState.copy(
            verificationId = verId,
            resendToken = resToken
        )

    }

    fun toggleErrorDialog(setToVisible: Boolean, text: String= "Something went wrong, try later", onErrorClose:()->Unit = { Unit }){
        uiState = uiState.copy(
            errorText = text,
            showError = setToVisible,
            isLoading = false,
            onErrorCloseAction = onErrorClose
        )
    }

    fun toggleLoadingCmp(setToVisible: Boolean?){
        uiState = uiState.copy(
            isLoading = setToVisible ?: !uiState.isLoading
        )
    }




}