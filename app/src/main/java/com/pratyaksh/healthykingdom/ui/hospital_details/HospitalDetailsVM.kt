package com.pratyaksh.healthykingdom.ui.hospital_details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.use_case.getFluidsData.GetFluidsByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HospitalDetailsVM @Inject constructor(
    private val getHospitalById: GetHospitalByIdUseCase,
    private val getHospitalFluidUseCase: GetFluidsByHospitalUseCase
): ViewModel() {

//    val hospital: MutableState<Users.Hospital?> = mutableStateOf(null)
    var uiState: MutableState<HospitalDetailsUiState> = mutableStateOf(HospitalDetailsUiState())
        private set

    fun fetchHospital(id: String){

        viewModelScope.launch {
            uiState.value = uiState.value.copy(hospital = getHospitalById(id))
        }

        viewModelScope.launch {
            getHospitalFluidUseCase(id).collectLatest {
                when(it){
                    is Resource.Error -> toggleError(true)
                    is Resource.Loading -> toggleLoading(true)
                    is Resource.Success -> {
                        uiState.value = uiState.value.copy(
                            bloods = it.data?.bloods!!,
                            plasma = it.data.plasma,
                            platelets = it.data.platelets
                        )
                    }
                }
            }
        }


    }

    fun toggleError(isError: Boolean){
        uiState.value = uiState.value.copy(isError = isError, isLoading = false)
    }
    fun toggleLoading(isLoading: Boolean){
        uiState.value = uiState.value.copy(isLoading = true)
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