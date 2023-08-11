package com.pratyaksh.healthykingdom.ui.user_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.use_case.add_ambulance.LoginAmbulanceUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.HospitalLoginUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.GetAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.GetPublicUserById
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.PublicUserLoginUseCase
import com.pratyaksh.healthykingdom.domain.use_case.settings.UpdateSettingUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginScreenVM @Inject constructor(
    private val hospitalLoginUseCase: HospitalLoginUseCase,
    private val ambulanceLoginUseCase: LoginAmbulanceUseCase,
    private val publicUserLoginUseCase: PublicUserLoginUseCase,
    private val addLocalSettingsDb: UpdateSettingUseCase,
    private val getAmbulanceUserCase: GetAmbulanceUserCase,
    private val getPublicUserUserCase: GetPublicUserById
) : ViewModel() {

    var uiState by mutableStateOf(LoginScreenUiState())
        private set

    fun onPhoneChange(newValue: String) {
        uiState = uiState.copy(
            phone = newValue
        )
    }

    fun onPassChange(newValue: String) {
        uiState = uiState.copy(
            password = newValue
        )
    }

    fun addLocalSettings(userId: String) {
        runBlocking {
            if (uiState.accountType.equals(AccountTypes.AMBULANCE)) {
                getAmbulanceUserCase.getAmbulanceByUserId(userId).last().let {
                    if (it is Resource.Success) {
                        addLocalSettingsDb.addLocalSettings(
                            userId,
                            false,
                            it.data!!.isOnline
                        )
                    } else {
                        toggleErrorDialog(
                            false,
                            "Unexpected error, make sure you are connected to internet"
                        )
                    }
                }
            } else if (uiState.accountType.equals(AccountTypes.HOSPITAL)) {
                addLocalSettingsDb.addLocalSettings(
                    userId,
                    false,
                    false
                )
            } else if (uiState.accountType.equals(AccountTypes.PUBLIC_USER)) {
                getPublicUserUserCase(userId).last().let {
                    if (it is Resource.Success) {
                        addLocalSettingsDb.addLocalSettings(
                            userId,
                            false,
                            it.data!!.providesLocation ?: false
                        )
                    } else {
                        toggleErrorDialog(
                            false,
                            "Unexpected error, make sure you are connected to internet"
                        )
                    }
                }
            } else {
                toggleErrorDialog(true, "Unexpected error")
            }
        }

    }


    fun toggleErrorDialog(setToVisible: Boolean, text: String = "", onErrorClose: () -> Unit={Unit}) {
        uiState = uiState.copy(
            isLoading = false,
            showError = setToVisible,
            errorText = text,
            onErrorCloseAction = onErrorClose
        )

    }

    fun onAccChange(newValue: AccountTypes) {
        uiState = uiState.copy(
            accountType = newValue
        )
    }

    fun toggleAccMenu(expand: Boolean? = null) {
        uiState = uiState.copy(
            isAccMenuExpanded = expand ?: !uiState.isAccMenuExpanded
        )
    }

    fun toggleLoadingCmp(setToVisible: Boolean? = null) {
        uiState = uiState.copy(
            isLoading = setToVisible ?: !uiState.isLoading
        )
    }

    fun onLogin() {
        viewModelScope.launch {
            uiState = uiState.copy(
                loginStatus = when (uiState.accountType) {
                    AccountTypes.HOSPITAL -> hospitalLoginUseCase(uiState.phone, uiState.password)
                    AccountTypes.PUBLIC_USER -> publicUserLoginUseCase(
                        uiState.phone,
                        uiState.password
                    )

                    AccountTypes.AMBULANCE -> ambulanceLoginUseCase(uiState.phone, uiState.password)
                }
            )
        }
    }


}