package com.pratyaksh.healthykingdom.ui.user_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.use_case.add_ambulance.LoginAmbulanceUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.HospitalLoginUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.PublicUserLoginUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenVM @Inject constructor(
    private val hospitalLoginUseCase: HospitalLoginUseCase,
    private val ambulanceLoginUseCase: LoginAmbulanceUseCase,
    private val publicUserLoginUseCase: PublicUserLoginUseCase,
): ViewModel() {

    var uiState by mutableStateOf(LoginScreenUiState())
        private set

    fun onPhoneChange(newValue: String){
        uiState = uiState.copy(
            phone= newValue
        )
    }
    fun onPassChange(newValue: String){
        uiState = uiState.copy(
            password = newValue
        )
    }
    fun onAccChange(newValue: AccountTypes){
        uiState = uiState.copy(
            accountType = newValue
        )
    }
    fun toggleAccMenu(expand: Boolean? = null){
        uiState = uiState.copy(
            isAccMenuExpanded = expand ?: !uiState.isAccMenuExpanded
        )
    }

    fun toggleLoadingCmp(setToVisible: Boolean? = null){
        uiState = uiState.copy(
            isLoading = setToVisible ?: !uiState.isLoading
        )
    }


    fun onLogin(){
        viewModelScope.launch {
            uiState = uiState.copy(
                loginStatus = when(uiState.accountType){
                     AccountTypes.HOSPITAL -> hospitalLoginUseCase(uiState.phone, uiState.password)
                     AccountTypes.PUBLIC_USER -> publicUserLoginUseCase(uiState.phone, uiState.password)
                     AccountTypes.AMBULANCE -> ambulanceLoginUseCase(uiState.phone, uiState.password)
                }
            )
        }
    }


}