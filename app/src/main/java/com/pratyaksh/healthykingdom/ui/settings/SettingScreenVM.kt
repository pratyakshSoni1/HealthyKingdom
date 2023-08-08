package com.pratyaksh.healthykingdom.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.use_case.ambulance_live_loc.UpdateAmbulanceLivePermit
import com.pratyaksh.healthykingdom.domain.use_case.delete_user.DeleteUserUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.GetAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.get_public_user.GetPublicUserById
import com.pratyaksh.healthykingdom.domain.use_case.settings.GetSettingsUseCase
import com.pratyaksh.healthykingdom.domain.use_case.settings.UpdateSettingUseCase
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.identifyUserTypeFromId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingScreenVM @Inject constructor(
    private val updateLocalSettingUseCase: UpdateSettingUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getHospitalByIdUseCase: GetHospitalByIdUseCase,
    private val getPublicUserById: GetPublicUserById,
    private val getAmbulanceUserCase: GetAmbulanceUserCase,
    private val updateLivePermitUseCase: UpdateAmbulanceLivePermit
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState as StateFlow<SettingsUiState>

    var isInitialized: Boolean = false

    fun initVM(userIdFlow: Flow<Resource<String?>>) {
        var userId: String?
        runBlocking {
            userId = userIdFlow.last().data
            Log.d("DEBUG_SETTINGS", "Got id $userId")
        }

        if (userId == null) {
            Log.d("INIT_SETTING_USER", "Got Id: $userId")
            toggleError(true)
            return Unit
        }
        viewModelScope.launch {
            toggleLoading(true)
            try {
                syncCloudSettingsToLocal(userId!!)
                _uiState.update {
                    Log.d("DEBUG", "ID: $userId")
                    it.copy(
                        userId = userId,
                        showLiveLocPermit = getSettingsUseCase.showLiveLocAllowed(userId!!),
                        goLive = getSettingsUseCase.isGoingLive(userId!!)
                    )
                }
                toggleLoading(false)
                isInitialized = true
            } catch (e: Exception) {
                toggleError(true)
                e.printStackTrace()
            }
        }
    }

    suspend fun syncCloudSettingsToLocal(userId: String){
            if(identifyUserTypeFromId(userId)!!.equals(AccountTypes.AMBULANCE)){
                getAmbulanceUserCase.getAmbulanceByUserId(userId).last().let{
                    if(it is Resource.Success){
                        updateLocalSettingUseCase.updateSettings(
                            userId,
                            false,
                            it.data!!.isOnline
                        )
                    }else{
                        toggleError(true)
                    }
                }
            } else if(identifyUserTypeFromId(userId)!!.equals(AccountTypes.HOSPITAL)){
                getHospitalByIdUseCase(userId).last().let{
                    if(it is Resource.Success){
                        updateLocalSettingUseCase.updateSettings(
                            userId,
                            false,
                            false
                        )
                    }else{
                        toggleError(true)
                    }
                }
            }else if(identifyUserTypeFromId(userId)!!.equals(AccountTypes.PUBLIC_USER)){
                getPublicUserById(userId).last().let{
                    if(it is Resource.Success){
                        updateLocalSettingUseCase.updateSettings(
                            userId,
                            it.data!!.providesLocation ?: false,
                            false
                        )
                    }else{
                        toggleError(true)
                    }
                }
            }
    }

    fun refreshUiState() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    goLive = getSettingsUseCase.isGoingLive(it.userId!!),
                    showLiveLocPermit = getSettingsUseCase.showLiveLocAllowed(it.userId),
                )
            }
        }
    }


    fun updateGoLive(setToLive: Boolean) {
        viewModelScope.launch {
            getAmbulanceUserCase.getAmbulanceByUserId(uiState.value.userId!!).last().let {
                if (it is Resource.Success) {
                    updateLivePermitUseCase(uiState.value.userId!!, setToLive).last().let {
                        if (!(it is Resource.Success))
                            toggleError(true)
                        else
                            Log.d("SETTINGS", "Updated fb settings")
                    }
                } else
                    toggleError(true)
            }
            refreshUiState()
        }
        viewModelScope.launch {
            updateLocalSettingUseCase.setGoLive(
                userId = uiState.value.userId!!,
                setToLive
            )
        }

    }

    fun updateShareLocPermit(permit: Boolean) {
        viewModelScope.launch {
            updateLocalSettingUseCase.setShowLiveLocPermission(
                userId = uiState.value.userId!!,
                permit
            )
        }
        refreshUiState()
    }

    fun toggleLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun toggleError(isError: Boolean) {
        _uiState.update {
            it.copy(isError = isError, isLoading = false)
        }
    }

    fun toggleVerifyDialog(setToVisible: Boolean) {
        _uiState.update {
            it.copy(showVerifyPassDialog = setToVisible)
        }
    }


    fun updateUserLogoutToFB(): Boolean {
        var isSuccess = false
        if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
            runBlocking {
                updateLivePermitUseCase(uiState.value.userId!!, false).last().let {
                    if (!(it is Resource.Success))
                        Log.d("LogOut Error", "${it.msg}")
                    else
                        isSuccess = true
                }
            }
            return isSuccess
        } else {
            return true
        }
    }

    fun updatePassTxt(newTxt: String) {

        _uiState.update {
            it.copy(verifyDialogPassTxt = newTxt)
        }
    }

    fun isAccVerifiedAndDeleted(): Flow<Resource<Boolean>> = flow {
        var isSuccess: Boolean = false
        toggleLoading(true)
        if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
            getAmbulanceUserCase.getAmbulanceByUserId(uiState.value.userId!!).last().let {
                if (it is Resource.Success) {
                    if (it.data!!.password!! == uiState.value.verifyDialogPassTxt) {
                        deleteUserUseCase.deleteAmbulance(uiState.value.userId!!).last().let {
                            if ( it is Resource.Success)
                                isSuccess = true
                        }
                    }
                } else {
                    toggleError(true)
                }
            }
        } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.HOSPITAL)) {
            getHospitalByIdUseCase(uiState.value.userId!!).last().let {
                if (it is Resource.Success) {
                    if (it.data!!.password!! == uiState.value.verifyDialogPassTxt) {
                        deleteUserUseCase.deleteHospital(uiState.value.userId!!).last().let {
                            if ( it is Resource.Success)
                                isSuccess = true
                        }
                    }
                } else {
                    toggleError(true)
                }
            }
        } else if (identifyUserTypeFromId(uiState.value.userId!!)!!.equals(AccountTypes.PUBLIC_USER)) {
            getPublicUserById(uiState.value.userId!!).last().let {
                if (it is Resource.Success) {
                    if (it.data!!.password!! == uiState.value.verifyDialogPassTxt) {
                        deleteUserUseCase.deletePublicUser(uiState.value.userId!!).last().let {
                            if ( it is Resource.Success)
                                isSuccess = true
                        }
                    }
                } else {
                    toggleError(true)
                }
            }
        }
        if(isSuccess)
            emit(Resource.Success(true))
        else
            emit(Resource.Error("Wrong Password !"))
        toggleLoading(false)

    }

    fun dismissPassVerification() {
        toggleVerifyDialog(false)
    }

    fun deleteLocalUserSettings(){
        toggleLoading(true)
        runBlocking{
            updateLocalSettingUseCase.deleteSettings(uiState.value.userId!!)
        }
    }


}