package com.pratyaksh.healthykingdom.ui.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.GetAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.GetPublicUserById
import com.pratyaksh.healthykingdom.domain.use_case.number_verification.OtpSendUseCase
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
class ChangePasswordVM @Inject constructor(
    val updatePasswordUseCase: UpdatePasswordUseCase,
    val getAmbulance: GetAmbulanceUserCase,
    val getHospital: GetHospitalByIdUseCase,
    val getPublicUserById: GetPublicUserById,
    val sendOtpUseCase: OtpSendUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePassScreenUiState())
    val uiState = _uiState as StateFlow<ChangePassScreenUiState>

    fun initScree( getCurrentUser: Flow<Resource<String?>>) {
        toggleLoading(true)
        runBlocking {
            getCurrentUser.last().let{res ->
                var phone = ""
                if( res is Resource.Success) {
                    if(identifyUserTypeFromId(res.data!!)!!.equals(AccountTypes.AMBULANCE)){
                        getAmbulance.getAmbulanceByUserId(res.data).last().let {
                            if (it is Resource.Success)
                                phone= it.data?.phone!!
                            else
                                toggleError(true, "can't fetch user, make sure you are connected to internet")
                        }
                    }else if(identifyUserTypeFromId(res.data!!)!!.equals(AccountTypes.HOSPITAL)){
                        getHospital(res.data).last().let {
                            if (it is Resource.Success)
                                phone= it.data?.phone!!
                            else
                                toggleError(true, "can't fetch user, make sure you are connected to internet")
                        }

                    }else if(identifyUserTypeFromId(res.data!!)!!.equals(AccountTypes.PUBLIC_USER)){
                        getPublicUserById(res.data).last().let {
                            if (it is Resource.Success)
                                phone= it.data?.phone!!
                            else
                                toggleError(true, "can't fetch user, make sure you are connected to internet")
                        }

                    } else{
                        toggleError(true, "Unexpected error !")
                    }
                    _uiState.update {
                        it.copy(userId = res.data!!, phone = phone)
                    }
                    toggleLoading(false)
                }
                else
                    toggleError(true, "Can't retrieve user")
            }
        }
    }

    fun verifyPasswordPattern(): Boolean {
        val alphaPattern = Regex("[a-zA-Z]+")
        val numPattern = Regex("[0-9]+")
        val symbolPattern = Regex("""[&|â€œ|`|Â´|}|{|Â°|>|<|:|.|;|#|'|)|(|@|_|$|"|!|?|*|=|^|-]+""")

        return (
                uiState.value.newPassTxt.contains(alphaPattern) &&
                        uiState.value.newPassTxt.contains(numPattern) &&
                        uiState.value.newPassTxt.contains(symbolPattern)
                )

    }

    fun onChangePassword(isOtpVerified: Boolean = false): Job {
        toggleLoading(true)
        return viewModelScope.launch {
            if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
                getAmbulance.getAmbulanceByUserId(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                        if ((uiState.value.oldPassTxt == (it.data!!.password ?: "")) || isOtpVerified) {
                            updatePasswordUseCase.updateAmbulancePassword(
                                userId = uiState.value.userId!!,
                                uiState.value.newPassTxt
                            ).last().let {
                                if (it is Resource.Success)
                                    toggleLoading(false)
                                else
                                    toggleError(true, it.msg ?: "Error updating password"){
                                        toggleError(false)
                                    }
                            }
                        }else toggleError(true, "Old password didn't match !"){
                            toggleError(false)
                        }
                    } else toggleError(true, "Unexpected error, try again later"){
                        toggleError(false)
                    }
                }
            } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.HOSPITAL)) {
                getHospital(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                        if ((uiState.value.oldPassTxt == (it.data!!.password ?: "")) || isOtpVerified) {
                            updatePasswordUseCase.updateHospitalPassword(
                                userId = uiState.value.userId!!,
                                uiState.value.newPassTxt
                            ).last().let {
                                if (it is Resource.Success)
                                    toggleLoading(false)
                                else
                                    toggleError(true, it.msg ?: "Error updating password"){
                                        toggleError(false)
                                    }
                            }
                        }else toggleError(true, "Old password didn't match !"){
                            toggleError(false)
                        }
                    } else toggleError(true, "Unexpected error, try again later"){
                        toggleError(false)
                    }
                }
            } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.PUBLIC_USER)) {
                getPublicUserById(uiState.value.userId!!).last().let {
                    if (it is Resource.Success) {
                        if ((uiState.value.oldPassTxt == (it.data!!.password ?: "")) || isOtpVerified) {
                            updatePasswordUseCase.updatePublicUserPassword(
                                userId = uiState.value.userId!!,
                                uiState.value.newPassTxt
                            ).last().let {
                                if (it is Resource.Success)
                                    toggleLoading(false)
                                else
                                    toggleError(true, it.msg ?: "Error updating password"){
                                        toggleError(false)
                                    }
                            }
                        }else toggleError(true, "Old password didn't match !"){
                            toggleError(false)
                        }
                    } else toggleError(true, "Unexpected error, try again later"){
                        toggleError(false)
                    }
                }
            } else {
                toggleError(true, "Unable to fetch user, try again later"){
                    toggleError(false)
                }
            }

        }
    }

    fun toggleError(setToVisible: Boolean, errorTxt: String = "", onErrorClose:()->Unit = {Unit}) {
        _uiState.update {
            it.copy(
                isLoading = if (setToVisible) false else it.isLoading,
                isError = setToVisible,
                errorTxt = errorTxt,
                onErrorCloseAction = onErrorClose
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
            it.copy(newPassTxt = newvalue)
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

data class ChangePassScreenUiState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val userId: String? = null,
    val oldPassTxt: String = "",
    val newPassTxt: String = "",
    val errorTxt: String = "",
    val verificationId: String? = null,
    val phone: String = "",
    val resendToken: PhoneAuthProvider.ForceResendingToken? = null,
    val onErrorCloseAction: ()->Unit = { Unit }
)