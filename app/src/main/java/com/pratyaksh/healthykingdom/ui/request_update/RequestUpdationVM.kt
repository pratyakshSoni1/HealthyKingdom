package com.pratyaksh.healthykingdom.ui.request_update

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.domain.use_case.get_requests.GetRequestByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.update_requests.UpdateRequestsUseCase
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestUpdationScrUiState())
    val uiState = _uiState as StateFlow<RequestUpdationScrUiState>

    fun initViewModel(hospitalId: String?){
        fetchHospitalAndReq(hospitalId)

    }

    fun toggleBloodRequest(
        group: BloodGroupsInfo,
    ) {
        _uiState.update {
            if (it.bloodRequest.contains(group)) {
                val updatedList = mutableListOf<BloodGroupsInfo>()
                updatedList.addAll(it.bloodRequest.filter { it != group })
                it.copy(bloodRequest = updatedList)
            } else {
                val updatedList = mutableListOf<BloodGroupsInfo>()
                updatedList.addAll(it.bloodRequest)
                updatedList.add(group)
                it.copy(bloodRequest = updatedList)
            }
        }
    }

    fun togglePlasmaRequest(
        group: PlasmaGroupInfo,
    ) {
        _uiState.update {
            if (it.plasmaRequest.contains(group)) {
                val updatedList = mutableListOf<PlasmaGroupInfo>()
                updatedList.addAll(it.plasmaRequest.filter { it != group })
                it.copy(plasmaRequest = updatedList)
            } else {
                val updatedList = mutableListOf<PlasmaGroupInfo>()
                updatedList.addAll(it.plasmaRequest)
                updatedList.add(group)
                it.copy(plasmaRequest = updatedList)
            }
        }
    }

    fun togglePlateletsRequest(
        group: PlateletsGroupInfo,
    ) {
        _uiState.update {
            if (it.plateletsRequest.contains(group)) {
                val updatedList = mutableListOf<PlateletsGroupInfo>()
                updatedList.addAll(it.plateletsRequest.filter { it != group })
                it.copy(plateletsRequest = updatedList)
            } else {
                val updatedList = mutableListOf<PlateletsGroupInfo>()
                updatedList.addAll(it.plateletsRequest)
                updatedList.add(group)
                it.copy(plateletsRequest =  updatedList )
            }
        }
    }

    fun toggleUpdateBtnState(setActive: Boolean) {
        _uiState.update {
            it.copy(isUpdationActive = setActive)
        }
    }

    fun toggleLoading(setVisible: Boolean) {
        _uiState.update {
            it.copy(isLoading = setVisible, isUpdationActive = !setVisible)
        }
    }

    fun toggleError(setVisible: Boolean? = null, text: String = "Unexpected error occured !") {
        _uiState.update {
            it.copy(
                isError = setVisible ?: !it.isError,
                errorTxt = text,
                isLoading = false,
                isUpdationActive = false
            )
        }
    }

    /** Only fluidType provided shouldn't be null **/
    fun showGroupInfo(
        fluidType: LifeFluids,
        plateletsGroupInfo: PlateletsGroupInfo? = null,
        bloodGroupInfo: BloodGroupsInfo? = null,
        plasmaGroupInfo: PlasmaGroupInfo? = null,
    ) {

        _uiState.update {
            it.copy(
                dialogFluidType = fluidType,
                dialogPlateletsGroup = plateletsGroupInfo,
                dialogBloodGroup = bloodGroupInfo,
                dialogPlasmaGroup = plasmaGroupInfo,
                showGroupInfoDialog = true
            )
        }
    }

    fun toggleSuccess(setVisible: Boolean? = null) {
        _uiState.update {
            it.copy(
                showSuccessDialog = setVisible ?: !it.showSuccessDialog,
                isLoading = false,
            )
        }
    }

    fun updateRequests() {
        toggleLoading(true)
        reqUpdtaeUseCase(
            Requests(
                uiState.value.hospitalId!!,
                uiState.value.bloodRequest,
                uiState.value.plasmaRequest,
                uiState.value.plateletsRequest
            )
        ).onEach {

            when (it) {
                is Resource.Error -> toggleError(true)
                is Resource.Loading -> toggleLoading(true)
                is Resource.Success -> {
                    toggleSuccess(true)
                    toggleUpdateBtnState(true)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun fetchHospitalAndReq(
        hospitalId: String?
    ) {
        if (hospitalId == null) {
            toggleError(true)
        } else {
            _uiState.update {
                it.copy(
                    hospitalId = hospitalId
                )
            }
        }
        fetchRequests()
    }

    fun fetchRequests() {
        getRequestByHospitalUseCase(
            uiState.value.hospitalId!!,
        ).onEach {

            when (it) {
                is Resource.Error -> toggleError(true)
                is Resource.Loading -> toggleLoading(true)
                is Resource.Success -> {
                    Log.d("DEBUG", "Received requests list")
                    if( it.data != null ) {
                        _uiState.update { tmpUiState ->
                            tmpUiState.copy(
                                bloodRequest = it.data.blood as MutableList,
                                plateletsRequest = it.data.platelets as MutableList,
                                plasmaRequest = it.data.plasma as MutableList,
                            )
                        }
                    }else{
                        toggleError(setVisible = true)
                    }
                    toggleLoading(false)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun dismissFluidDialog() {
        _uiState.update {
            it.copy(
                showGroupInfoDialog = false
            )
        }
    }


}