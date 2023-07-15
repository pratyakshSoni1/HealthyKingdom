package com.pratyaksh.healthykingdom.ui.request_update

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.domain.use_case.get_requests.GetRequestByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.update_requests.UpdateRequestsUseCase
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.toBloodGroupsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RequestUpdationVM @Inject constructor(
    val reqUpdtaeUseCase: UpdateRequestsUseCase,
    val getRequestByHospitalUseCase: GetRequestByHospitalUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow( RequestUpdationScrUiState() )
    val uiState = _uiState as StateFlow<RequestUpdationScrUiState>


    fun toggleBloodRequest(
        group: BloodGroupsInfo,
    ){
        _uiState.update {
            if( it.bloodRequest.contains(group) ) {
                it.copy( bloodRequest = it.bloodRequest.filter { it != group }  )
            }else{
                it.copy( bloodRequest = it.bloodRequest + group )
            }
        }
    }

    fun togglePlasmaRequest(
        group: PlasmaGroupInfo,
    ){
        _uiState.update {
            if( it.plasmaRequest.contains(group) ) {
                it.copy( plasmaRequest = it.plasmaRequest.filter { it != group }  )
            }else{
                it.copy( plasmaRequest = it.plasmaRequest + group )
            }
        }
    }

    fun togglePlateletsRequest(
        group: PlateletsGroupInfo,
    ){
        _uiState.update {
            if( it.plateletsRequest.contains(group) ) {
                it.copy( plateletsRequest = it.plateletsRequest.filter { it != group }  )
            }else{
                it.copy( plateletsRequest = it.plateletsRequest + group )
            }
        }
    }

    fun toggleUpdateBtnState(setActive: Boolean){
        _uiState.update {
            it.copy( isUpdationActive = setActive)
        }
    }

    fun toggleLoading(setVisible: Boolean? = null){
        _uiState.update{ it.copy( isLoading= setVisible ?: !it.isLoading) }
    }

    fun toggleError(setVisible: Boolean? = null, text: String= "Unexpected error occured !"){
        _uiState.update{ it.copy(
            isError = setVisible ?: !it.isError,
            errorTxt = text,
            isLoading = false
        ) }
    }

    fun toggleSuccess(setVisible: Boolean? = null){
        _uiState.update{ it.copy(
            showSuccessDialog = setVisible ?: !it.showSuccessDialog,
            isLoading = false
        ) }
    }

    fun updateRequests(){
        reqUpdtaeUseCase(
            Requests(
                uiState.value.hospitalId!!,
                uiState.value.bloodRequest,
                uiState.value.plasmaRequest,
                uiState.value.plateletsRequest.map { it.toBloodGroupsInfo() }
            )
        ).onEach {

            when (it){
                is Resource.Error -> toggleError(true)
                is Resource.Loading -> toggleLoading(true)
                is Resource.Success -> toggleSuccess(true)
            }

        }.launchIn(viewModelScope)
    }

    fun fetchRequests(){
        getRequestByHospitalUseCase(
            uiState.value.hospitalId!!,
        ).onEach {

            when (it){
                is Resource.Error -> toggleError(true)
                is Resource.Loading -> toggleLoading(true)
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            bloodRequest = it.bloodRequest,
                            plateletsRequest = it.plateletsRequest,
                            plasmaRequest = it.plasmaRequest,
                        )
                    }
                    toggleLoading(false)
                }
            }

        }.launchIn(viewModelScope)
    }


}