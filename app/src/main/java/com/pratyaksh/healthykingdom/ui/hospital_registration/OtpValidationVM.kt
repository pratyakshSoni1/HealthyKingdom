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
import com.pratyaksh.healthykingdom.domain.use_case.add_hospital.AddHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class OtpValidationVM @Inject constructor(
    val otpSignInUseCase: OtpSendUseCase,
    private val addHospitalUseCase: AddHospitalUseCase
) : ViewModel() {

    var uiState by mutableStateOf(OtpValidationUiState())
        private set

    fun onCodeChange(newVal: String){
        uiState = uiState.copy(
            code = newVal
        )
    }

    fun initScreen(verificationId: String, phoneNum: String, resendingToken: ForceResendingToken, hospitalDto: HospitalsDto){
        uiState = uiState.copy(
            verificationId = verificationId,
            phone = phoneNum,
            resendToken= resendingToken,
            hospitalDto = hospitalDto,
        )
        activateTimeout()
    }

    fun activateTimeout(){
        uiState = uiState.copy(resendTimeout = 0)

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
        uiState = uiState.copy(isTimoutRunning = false)
    }

    fun updateVerificationIdAndToken(verId:String, resToken: PhoneAuthProvider.ForceResendingToken){

        uiState = uiState.copy(
            verificationId = verId,
            resendToken = resToken
        )

    }

    fun addHospitalToFB(){
        viewModelScope.launch {
            addHospitalUseCase(uiState.hospitalDto!!)
                .collectLatest {
                    Log.d("VMLOGS","Adding hospital: success - ${it.data}")
                }
        }
    }



}