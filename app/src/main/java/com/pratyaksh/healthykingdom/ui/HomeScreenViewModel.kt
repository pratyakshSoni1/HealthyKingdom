package com.pratyaksh.healthykingdom.ui


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.use_case.get_all_hospitals.GetAllHospitalsUseCase
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet.MarkerDetailSheetUiState
import com.pratyaksh.healthykingdom.utils.*
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val getAllHospitalsUseCase: GetAllHospitalsUseCase
): ViewModel() {

    val allHospitals = mutableStateListOf<Hospital>()
    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf(false)

    val detailSheetUiState = mutableStateOf( MarkerDetailSheetUiState(
        isLoading = false,
        hospitalName = "First",
        listOf(BloodGroupsInfo.B_NEGATIVE, BloodGroupsInfo.B_NEGATIVE),
        listOf(PlasmaGroupInfo.PLASMA_O, PlasmaGroupInfo.PLASMA_AB),
        listOf(),
    ) )

    init {
        getAllHospitals()
    }

    fun getAllHospitals(){

        getAllHospitalsUseCase().onEach {
            when(it){

                is Resource.Success -> {
                    allHospitals.clear()
                    allHospitals.addAll( it.data ?: emptyList() )
                    Log.d("ViewmodelLogs", "Hospitals retreived: ${it.data}")
                }
                is Resource.Loading -> {
                    isLoading.value = true
                }
                is Resource.Error -> {
                    isLoading.value = false
                    isError.value = true
                }

            }
        }.launchIn(viewModelScope)

    }

}