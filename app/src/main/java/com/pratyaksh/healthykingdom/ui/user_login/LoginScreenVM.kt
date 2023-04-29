package com.pratyaksh.healthykingdom.ui.user_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByPhoneUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class LoginScreenVM @Inject constructor(
    private val hospitalByPhoneUseCase: GetHospitalByPhoneUseCase
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
        uiState = uiState.copy(
            loginStatus = flow {
                emit(Resource.Loading("loading", "Logging in..."))
                delay(3000L)

                val res = hospitalByPhoneUseCase(uiState.phone, uiState.password)
                if(res != null) emit(Resource.Success(res.id))
                else emit(Resource.Error("Login Failed"))
            }
        )

    }


}