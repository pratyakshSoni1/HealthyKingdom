package com.pratyaksh.healthykingdom.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.toAmbulanceDto
import com.pratyaksh.healthykingdom.domain.use_case.add_ambulance.AddAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.ambulance_live_loc.UpdateAmbulanceLivePermit
import com.pratyaksh.healthykingdom.domain.use_case.delete_user.DeleteUserUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.GetAmbulanceUserCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.UpdateAmbulanceLocUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.UpdateAmbulanceUseCase
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
    val updateLocUseCase: UpdateAmbulanceLivePermit
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState as StateFlow<SettingsUiState>

    var isInitialized: Boolean = false

    fun initVM(userIdFlow: Flow<Resource<String?>>) {
        var userId: String?
        runBlocking{
            userId = userIdFlow.last().data
            Log.d("DEBUG_SETTINGS", "Got id $userId")
        }

        if(userId == null) {
            Log.d("INIT_SETTING_USER", "Got Id: $userId")
            toggleError(true)
            return Unit
        }
        viewModelScope.launch {
            toggleLoading(true)
            try {
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
        viewModelScope.launch{
            getAmbulanceUserCase.getAmbulanceByUserId(uiState.value.userId!!).last().let {
                if(it is Resource.Success) {
                    updateLocUseCase(uiState.value.userId!!, setToLive).last().let{
                        if (!(it is Resource.Success))
                            toggleError(true)
                        else
                            Log.d("SETTINGS", "Updated fb settings")
                    }
                }
                else
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

    fun verifyUser(password: String): Boolean{
        var isVerified: Boolean = false
        runBlocking{
            toggleLoading(true)
            if (identifyUserTypeFromId(userId = uiState.value.userId!!)!!.equals(AccountTypes.PUBLIC_USER)) {
                lateinit var tmpUser: Users.PublicUser
                getPublicUserById(uiState.value.userId!!).last().let{
                    if(it is Resource.Success) tmpUser = it.data!!
                    else toggleError(true)
                    isVerified = (tmpUser.password == password)
                }
            } else if (identifyUserTypeFromId(userId = uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
                lateinit var tmpUser: Users.Ambulance
                getAmbulanceUserCase.getAmbulanceByUserId(uiState.value.userId!!).last().let{
                    if(it is Resource.Success) tmpUser = it.data!!
                    else toggleError(true)
                    isVerified = (tmpUser.password == password)
                }
            } else if (identifyUserTypeFromId(userId = uiState.value.userId!!)!!.equals(AccountTypes.HOSPITAL)) {
                lateinit var tmpUser: Users.Hospital
                getHospitalByIdUseCase(uiState.value.userId!!).last().let{
                    if(it is Resource.Success) tmpUser = it.data!!
                    else toggleError(true)
                    isVerified = (tmpUser.password == password)
                }
            }
        }
        return isVerified
    }

    fun deleteUser() {
        runBlocking {
            toggleLoading(true)
            try{
                if (identifyUserTypeFromId(userId = uiState.value.userId!!)!!.equals(AccountTypes.PUBLIC_USER)) {
                    deleteUserUseCase.deletePublicUser(uiState.value.userId!!)
                } else if (identifyUserTypeFromId(userId = uiState.value.userId!!)!!.equals(AccountTypes.AMBULANCE)) {
                    deleteUserUseCase.deleteAmbulance(uiState.value.userId!!)
                } else if (identifyUserTypeFromId(userId = uiState.value.userId!!)!!.equals(AccountTypes.HOSPITAL)) {
                    deleteUserUseCase.deleteHospital(uiState.value.userId!!)
                }
                toggleLoading(false)
            }catch(e: Exception){
                e.printStackTrace()
                toggleError(true)
            }
        }
    }

    fun toggleVerifyDialog( setToVisible: Boolean ){
        _uiState.update {
            it.copy( showVerifyPassDialog = setToVisible )
        }
    }

    fun updateDialogPassTxt( newTxt: String ){
        _uiState.update {
            it.copy( verifyDialogPassTxt = newTxt )
        }
    }

}