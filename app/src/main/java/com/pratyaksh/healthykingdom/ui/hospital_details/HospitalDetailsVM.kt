package com.pratyaksh.healthykingdom.ui.hospital_details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HospitalDetailsVM @Inject constructor(
    val getHospitalById: GetHospitalByIdUseCase
): ViewModel() {

    val hospital: MutableState<Hospital?> = mutableStateOf(null)

    fun fetchHospital(id: String){
        viewModelScope.launch {
            hospital.value = getHospitalById(id)
        }
    }

}