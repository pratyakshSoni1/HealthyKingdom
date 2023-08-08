package com.pratyaksh.healthykingdom.ui.user_registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.use_case.add_ambulance.AddAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.add_hospital.AddHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.add_public_user.AddPublicUserCase
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class RegisterScreenVM @Inject constructor(
    val otpSendUseCase: OtpSendUseCase,
    private val addHospitalUseCase: AddHospitalUseCase,
    private val addPublicUseCse: AddPublicUserCase,
    private val addAmbulanceUseCase: AddAmbulanceUserCase,
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

    fun toggleAccMenu(expand: Boolean? = null){
        uiState = uiState.copy(
            isAccMenuExpanded = expand ?: !uiState.isAccMenuExpanded
        )
    }

    fun onAccChange(accountType: AccountTypes){
        uiState = uiState.copy(
            accountType = accountType
        )
    }
    fun onGenderChange(gender: Gender){
        uiState = uiState.copy(
            gender = gender
        )
    }
    fun toggleProvideLoc(provide: Boolean){
        uiState = uiState.copy(
            providesLocation = provide
        )
    }

    fun onAgeChange(age: String){
        uiState = uiState.copy(
            age= age
        )

    }
    fun onVehicleNumberChange(number: String){
        uiState = uiState.copy(
            vehicleNumber = number
        )

    }

    fun getUser(): Users {

        val userId = uiState.phone.substring(8..11)

        return when(uiState.accountType){
            AccountTypes.AMBULANCE -> Users.Ambulance(
                phone= uiState.phone,
                driverName = uiState.name,
                mail= uiState.mail,
                password = uiState.password,
                userId= "${AccountTypes.AMBULANCE.type}-${userId}",
                vehicleNumber = uiState.vehicleNumber,
                driverAge= uiState.age.toInt(),
                driverGender= when(uiState.gender){
                    Gender.MALE -> "M"
                    Gender.FEMALE -> "F"
                    Gender.OTHERS -> "OTH"
                },
                vehicleLocation = GeoPoint(0.0, 0.0),
                isVacant= false,
                isOnline = false,
                lastLocUpdated = null,
            )

            AccountTypes.HOSPITAL -> Users.Hospital(
                phone= uiState.phone,
                name= uiState.name,
                mail= uiState.mail,
                location = uiState.location!!,
                password = uiState.password,
                userId= "${AccountTypes.HOSPITAL.type}-${userId}"
            )

            AccountTypes.PUBLIC_USER -> Users.PublicUser(
                phone= uiState.phone,
                userName = uiState.name,
                mail= uiState.mail,
                location = uiState.location ?: GeoPoint(0.0, 0.0),
                userId= "${AccountTypes.PUBLIC_USER.type}-${userId}",
                providesLocation = uiState.providesLocation,
                password = uiState.password,
                gender = uiState.gender
            )

        }

    }

}