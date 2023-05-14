package com.pratyaksh.healthykingdom.ui.fluid_update

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.AvailFluids
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailBlood
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlasma
import com.pratyaksh.healthykingdom.domain.model.lifefluids.AvailPlatelets
import com.pratyaksh.healthykingdom.domain.model.lifefluids.LifeFluidsModel
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toBloodsModel
import com.pratyaksh.healthykingdom.domain.model.lifefluids.toPlateletsModel
import com.pratyaksh.healthykingdom.domain.use_case.getFluidsData.GetSpecificFluidByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.update_lifefluids.UpdateLifeFluidsUseCase
import com.pratyaksh.healthykingdom.utils.BloodGroups
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FluidUpdateScreenVM @Inject constructor(
    private val updateFluidUseCase: UpdateLifeFluidsUseCase,
    private val getFluidUseCase: GetSpecificFluidByHospitalUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(FluidUpdateScreenUiState())
    val uiState: StateFlow<FluidUpdateScreenUiState> = _uiState

    fun initScreen( userId: String, fluidType: LifeFluids ){
        _uiState.update {
            it.copy(fluidType = fluidType, currentUserId = userId)
        }

        viewModelScope.launch {
            toggleLoading(true)
            getFluidUseCase(
                _uiState.value.currentUserId!!,
                _uiState.value.fluidType!!
            ).collectLatest {resp ->
                delay(1000L)
                when(resp){
                    is Resource.Error -> { toggleError(true, resp.msg!!) }
                    is Resource.Loading -> toggleLoading(true)
                    is Resource.Success -> {
                        toggleLoading(false)

                        _uiState.update {
                            when(resp.data){
                                is AvailPlatelets -> it.copy(availBloodGroups = resp.data.toBloodsModel())
                                is AvailBlood -> it.copy(availBloodGroups = resp.data)
                                is AvailPlasma -> it.copy(availPlasma = resp.data)
                                else -> { it.copy() }
                            }

                        }

                    }
                }
            }
        }

    }

    fun incBloodGroupQty( group: BloodGroups ){
        _uiState.update {
            when (group) {
                BloodGroups.A_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(aPos= it.availBloodGroups.aPos+1))
                BloodGroups.A_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(aNeg= it.availBloodGroups.aNeg+1))
                BloodGroups.B_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(bPos= it.availBloodGroups.bPos+1))
                BloodGroups.B_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(bNeg= it.availBloodGroups.bNeg+1))
                BloodGroups.O_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(oPos= it.availBloodGroups.oPos+1))
                BloodGroups.O_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(oNeg= it.availBloodGroups.oNeg+1))
                BloodGroups.AB_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(abPos= it.availBloodGroups.abPos+1))
                BloodGroups.AB_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(abNeg= it.availBloodGroups.abNeg+1))
            }
        }
    }
    fun incPlasmaGroupQty( group: Plasma ) {
        _uiState.update {
            when (group) {
                Plasma.PLASMA_A -> it.copy(availPlasma = it.availPlasma.copy(aGroup = it.availPlasma.aGroup + 1))
                Plasma.PLASMA_AB -> it.copy(availPlasma = it.availPlasma.copy(abGroup = it.availPlasma.abGroup + 1))
                Plasma.PLASMA_B -> it.copy(availPlasma = it.availPlasma.copy(bGroup = it.availPlasma.bGroup + 1))
                Plasma.PLASMA_O -> it.copy(availPlasma = it.availPlasma.copy(oGroup = it.availPlasma.oGroup + 1))
            }
        }
    }
    fun decBloodGroupQty( group: BloodGroups ){
        _uiState.update {
            when (group) {
                BloodGroups.A_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(aPos= it.availBloodGroups.aPos-1))
                BloodGroups.A_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(aNeg= it.availBloodGroups.aNeg-1))
                BloodGroups.B_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(bPos= it.availBloodGroups.bPos-1))
                BloodGroups.B_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(bNeg= it.availBloodGroups.bNeg-1))
                BloodGroups.O_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(oPos= it.availBloodGroups.oPos-1))
                BloodGroups.O_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(oNeg= it.availBloodGroups.oNeg-1))
                BloodGroups.AB_POSITIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(abPos= it.availBloodGroups.abPos-1))
                BloodGroups.AB_NEGATIVE -> it.copy(availBloodGroups = it.availBloodGroups.copy(abNeg= it.availBloodGroups.abNeg-1))
            }
        }
    }
    fun decPlasmaGroupQty( group: Plasma ) {
        _uiState.update {
            when (group) {
                Plasma.PLASMA_A -> it.copy(availPlasma = it.availPlasma.copy(aGroup = it.availPlasma.aGroup - 1))
                Plasma.PLASMA_AB -> it.copy(availPlasma = it.availPlasma.copy(abGroup = it.availPlasma.abGroup - 1))
                Plasma.PLASMA_B -> it.copy(availPlasma = it.availPlasma.copy(bGroup = it.availPlasma.bGroup - 1))
                Plasma.PLASMA_O -> it.copy(availPlasma = it.availPlasma.copy(oGroup = it.availPlasma.oGroup - 1))
            }
        }
    }

    fun toggleLoading(setVisible: Boolean? = null){
        _uiState.update{ it.copy( isLoading= setVisible ?: !it.isLoading) }
    }
    fun toggleError(setVisible: Boolean? = null, text: String= "Unexpected error occured !"){
        _uiState.update{ it.copy(
            showErrorDialog = setVisible ?: !it.showErrorDialog,
            errorText = text,
            isLoading = false
        ) }
    }
    fun toggleSuccess(setVisible: Boolean? = null){
        _uiState.update{ it.copy(
            showSuccesssDialog = setVisible ?: !it.showSuccesssDialog,
            isLoading = false
        ) }
    }

    fun onUpdateFluidToFireStore(){

        Log.d("UpdatingFluidLogs", "Clicked update")
        viewModelScope.launch {
            toggleLoading(true)
            updateFluidUseCase(
                _uiState.value.fluidType!!,
                fluidData = AvailFluids(
                    _uiState.value.availBloodGroups,
                    _uiState.value.availPlasma,
                    _uiState.value.availBloodGroups.toPlateletsModel()
                ),
                hospitalId = _uiState.value.currentUserId!!
            ).collectLatest{
                when(it){
                    is Resource.Error -> toggleError(true, it.msg!!)
                    is Resource.Loading -> toggleLoading(true)
                    is Resource.Success -> toggleSuccess(true)

                }
            }
        }

    }

}