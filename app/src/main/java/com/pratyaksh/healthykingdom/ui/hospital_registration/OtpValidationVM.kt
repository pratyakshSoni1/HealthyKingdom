package com.pratyaksh.healthykingdom.ui.hospital_registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.use_case.add_hospital.AddHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSignInUseCase
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class OtpValidationVM @Inject constructor(
    val otpSignInUseCase: OtpSignInUseCase,
    val otpSendUseCase: OtpSendUseCase,
    private val addHospitalUseCase: AddHospitalUseCase
) : ViewModel() {

    var uiState by mutableStateOf(OtpValidationUiState())
        private set

    fun onCodeChange(newVal: String){
        uiState = uiState.copy(
            code = newVal
        )
    }

    fun initScreen(verificationId: String, phoneNum: String, resendingToken: ForceResendingToken, user: Users){
        uiState = uiState.copy(
            verificationId = verificationId,
            phone = phoneNum,
            resendToken= resendingToken,
            user = user,
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

    fun toggleErrorDialog(setToVisible: Boolean, text: String= "Something went wrong, try later"){
        uiState = uiState.copy(
            errorText = text,
            showError = setToVisible,
            isLoading = false,
        )
    }

    fun toggleLoadingCmp(setToVisible: Boolean?){
        uiState = uiState.copy(
            isLoading = setToVisible ?: !uiState.isLoading
        )
    }

    fun addHospitalToFB(){
        viewModelScope.launch {

            when(uiState.user!!){
                is Users.Ambulance -> {

                }
                is Users.Hospital -> {
                    addHospitalUseCase(uiState.user)
                }
                is Users.PublicUser -> {

                }

            }

            addHospitalUseCase(uiState.user!!)
                .collectLatest {
                    when(it){
                        is Resource.Error -> {
                            toggleLoadingCmp(false)
                            toggleErrorDialog(true, it.msg!!)
                        }
                        is Resource.Loading -> {
                            toggleLoadingCmp(true)
                        }
                        is Resource.Success -> {
                            toggleLoadingCmp(false)
                            Log.d("VMLOGS","Adding hospital: success - ${it.data}")
                        }
                    }
                }
        }
    }



}