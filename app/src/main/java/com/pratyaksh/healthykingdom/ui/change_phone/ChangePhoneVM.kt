package com.pratyaksh.healthykingdom.ui.change_phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.GetAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.GetPublicUserById
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
import com.pratyaksh.healthykingdom.domain.use_case.update_users.UpdatePhoneUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.identifyUserTypeFromId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ChangePhoneVM @Inject constructor(
    val getHospital: GetHospitalByIdUseCase,
    val getAmbulance: GetAmbulanceUserCase,
    val getPublicUserById: GetPublicUserById,
    val updatePhoneUseCase: UpdatePhoneUseCase,
    val sendOtpSendUseCase: OtpSendUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePhoneScreenUiState())
    val uiState = _uiState as StateFlow<ChangePhoneScreenUiState>

    fun initScree( getCurrentUser: Flow<Resource<String?>>) {
        toggleLoading(true)
        runBlocking {
            getCurrentUser.last().let{res ->
                if( res is Resource.Success) {
                    _uiState.update {
                        it.copy(userId = res.data!!)
                    }
                    toggleLoading(false)
                }
                else
                    toggleError(true, "Can't retrieve user")
            }
        }
    }

    fun changePhone() {
        toggleLoading(true)
        viewModelScope.launch {
            if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
                getAmbulance.getAmbulanceByUserId(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                             updatePhoneUseCase.updateAmbulancePhone(
                                userId = uiState.value.userId!!,
                                uiState.value.newPhoneTxt
                            ).last().let {
                                if (it is Resource.Success)
                                    toggleLoading(false)
                                else
                                    toggleError(true, it.msg ?: "Error updating password")
                            }
                    } else toggleError(true, "Unexpected error, try again later")
                }
            } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.HOSPITAL)) {
                getHospital(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                            updatePhoneUseCase.updateHospitalPhone(
                                userId = uiState.value.userId!!,
                                uiState.value.newPhoneTxt
                            ).last().let {
                                if (it is Resource.Success)
                                    toggleLoading(false)
                                else
                                    toggleError(true, it.msg ?: "Error updating password")
                            }
                    } else toggleError(true, "Unexpected error, try again later")
                }
            } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.PUBLIC_USER)) {
                getPublicUserById(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                            updatePhoneUseCase.updatePublicUserPhone(
                                userId = uiState.value.userId!!,
                                uiState.value.newPhoneTxt
                            ).last().let {
                                if (it is Resource.Success)
                                    toggleLoading(false)
                                else
                                    toggleError(true, it.msg ?: "Error updating password")
                            }
                    } else toggleError(true, "Unexpected error, try again later")
                }
            } else {
                toggleError(true, "Unable to fetch user, try again later")
            }

        }
    }

    fun verifyPassword(): Boolean {
        toggleLoading(true)
        var isVerified = false
        runBlocking {
            if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
                getAmbulance.getAmbulanceByUserId(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                        isVerified = uiState.value.oldPassTxt == (it.data!!.password ?: "")
                    } else toggleError(true, "Unexpected error, try again later")
                }
            } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.HOSPITAL)) {
                getHospital(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                        isVerified = uiState.value.oldPassTxt == (it.data!!.password ?: "")
                    } else toggleError(true, "Unexpected error, try again later")
                }
            } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.PUBLIC_USER)) {
                getPublicUserById(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                        isVerified = uiState.value.oldPassTxt == (it.data!!.password ?: "")
                    } else toggleError(true, "Unexpected error, try again later")
                }
            } else {
                toggleError(true, "Unable to fetch user, try again later")
            }
        }

        return isVerified
    }

    fun toggleError(setToVisible: Boolean, errorTxt: String = "") {
        _uiState.update {
            it.copy(
                isLoading = if (setToVisible) false else it.isLoading,
                isError = setToVisible,
                errorTxt = errorTxt
            )
        }
    }

    fun toggleLoading(setToVisible: Boolean) {
        _uiState.update {
            it.copy(isLoading = setToVisible)
        }
    }

    fun updateOldPassTxt(newvalue: String) {
        _uiState.update {
            it.copy(oldPassTxt = newvalue)
        }
    }

    fun updateNewPassTxt(newvalue: String) {
        _uiState.update {
            it.copy(newPhoneTxt = newvalue)
        }
    }

    fun validateNewPhone() {
        if(uiState.value.newPhoneTxt.length == 12){
            if(uiState.value.newPhoneTxt.contains(Regex("^[0-9]*\$"))){
                toggleError(true, "Invalid phone number")
            }
        }else{
            toggleError(true, "Enter a valid number with your country code ( 91 for India )")
        }
    }


    fun updateVerificationId(id: String){
        _uiState.update {
            it.copy(
                verificationId = id
            )
        }
    }


}

data class ChangePhoneScreenUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val userId: String? = null,
    val oldPassTxt: String = "",
    val newPhoneTxt: String = "",
    val errorTxt: String = "",
    val verificationId: String? = null,
    val resendToken: PhoneAuthProvider.ForceResendingToken? = null,
)