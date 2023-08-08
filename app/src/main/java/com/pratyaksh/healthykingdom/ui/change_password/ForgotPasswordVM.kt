package com.pratyaksh.healthykingdom.ui.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.GetAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.GetPublicUserById
import com.pratyaksh.healthykingdom.domain.use_case.update_password.UpdatePasswordUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.identifyUserTypeFromId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordVM @Inject constructor(
    val getHospital: GetHospitalByIdUseCase,
    val getAmbulance: GetAmbulanceUserCase,
    val getPublicUserById: GetPublicUserById,
    val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPhoneScreenUiState())
    val uiState = _uiState as StateFlow<ForgotPhoneScreenUiState>

    fun initScreen( getCurrentUser: Flow<Resource<String?>>) {
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

    fun onChangePassword(): Job {
        toggleLoading(true)
        return viewModelScope.launch {
            if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
                getAmbulance.getAmbulanceByUserId(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                        updatePasswordUseCase.updateAmbulancePassword(
                                userId = uiState.value.userId!!,
                                uiState.value.newPassTxt
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
                        updatePasswordUseCase.updateHospitalPassword(
                                userId = uiState.value.userId!!,
                                uiState.value.newPassTxt
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
                        updatePasswordUseCase.updatePublicUserPassword(
                                userId = uiState.value.userId!!,
                                uiState.value.newPassTxt
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

    fun updateNewPassTxt(newvalue: String) {
        _uiState.update {
            it.copy(newPassTxt = newvalue)
        }
    }

}

data class ForgotPhoneScreenUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val userId: String? = null,
    val newPassTxt: String = "",
    val errorTxt: String = ""
)