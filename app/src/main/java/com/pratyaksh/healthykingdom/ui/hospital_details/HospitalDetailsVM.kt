package com.pratyaksh.healthykingdom.ui.hospital_details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.use_case.getFluidsData.GetFluidsByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_requests.GetRequestByHospitalUseCase
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HospitalDetailsVM @Inject constructor(
    private val getHospitalById: GetHospitalByIdUseCase,
    private val getHospitalFluidUseCase: GetFluidsByHospitalUseCase,
    private val getRequestByHospitalUseCase: GetRequestByHospitalUseCase
): ViewModel() {

    var uiState: MutableState<HospitalDetailsUiState> = mutableStateOf(HospitalDetailsUiState())
        private set

    fun fetchHospital(hospitalId: String){
            getHospitalById(hospitalId).onEach {
                when(it){
                    is Resource.Error -> {
                        toggleError(true, "Error getting logged user, please login again")
                    }
                    is Resource.Loading -> {
                        toggleLoading(true)
                    }
                    is Resource.Success -> {
                        toggleLoading(false)
                        uiState.value = uiState.value.copy(hospital = it.data)
                    }
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            getHospitalFluidUseCase(hospitalId).collectLatest {
                when(it){
                    is Resource.Error -> toggleError(true, "Error fetching fluid")
                    is Resource.Loading -> toggleLoading(true)
                    is Resource.Success -> {
                        if( it.data != null ) {
                            uiState.value = uiState.value.copy(
                                bloods = it.data.bloods,
                                plasma = it.data.plasma,
                                platelets = it.data.platelets
                            )
                            toggleLoading(false)
                            Log.d("DetailScreen", "Data Loading success + loading set false")
                        }else{
                            toggleError(true, "Unexpected Error while fetching fluids")
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            getRequestByHospitalUseCase(hospitalId).last().let {
                if(it is Resource.Success){
                    uiState.value = uiState.value.copy(
                        requests = it.data
                    )
                }else{
                    toggleError(true, "Unable fetch requests")
                }
            }
        }


    }

    fun toggleError(isError: Boolean, errorTxt: String){
        uiState.value = uiState.value.copy(isError = isError, isLoading = false, errorTxt = errorTxt)
    }
    fun toggleLoading(isLoading: Boolean){
        uiState.value = uiState.value.copy(isLoading = isLoading)
    }

    fun showFluidInfoDialog(
        fluidType: LifeFluids,
        fluidBloodGroup: BloodGroupsInfo? = null,
        fluidPlateletsGroup: PlateletsGroupInfo? = null,
        fluidPlasmaGroup: PlasmaGroupInfo? = null
    ){
        if( fluidBloodGroup != null || fluidPlasmaGroup != null ) {
            uiState.value = uiState.value.copy(
                dialogFluidType = fluidType,
                dialogBloodGroup = fluidBloodGroup,
                dialogPlateletsGroup = fluidPlateletsGroup,
                dialogPlasmaGroup = fluidPlasmaGroup,
                showFluidDialog = true
            )
        }
    }

    fun dismissFluidDialog(){
        uiState.value = uiState.value.copy(
            showFluidDialog = false
        )
    }

}