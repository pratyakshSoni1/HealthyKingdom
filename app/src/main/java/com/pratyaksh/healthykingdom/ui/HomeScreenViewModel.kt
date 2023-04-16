package com.pratyaksh.healthykingdom.ui


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.use_case.get_all_hospitals.GetAllHospitalsUseCase
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreenUiState
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet.MarkerDetailSheetUiState
import com.pratyaksh.healthykingdom.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val getAllHospitalsUseCase: GetAllHospitalsUseCase
): ViewModel() {

    val homeScreenUiState = mutableStateOf( HomeScreenUiState() )

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

    private fun getAllHospitals(){

        getAllHospitalsUseCase().onEach {
            when(it){

                is Resource.Success -> {

                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        hospitals = it.data ?: emptyList(),
                        isLoading = false
                    )
                    Log.d("ViewmodelLogs", "Hospitals retreived: ${it.data}")
                }
                is Resource.Loading -> {

                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {

                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        isLoading = false,
                        isError = true
                    )
                }

            }
        }.launchIn(viewModelScope)

    }

    fun setBottomSheetLoading( isLoading:Boolean = true ){
        detailSheetUiState.value = detailSheetUiState.value.copy(isLoading)
    }
    fun setBottomSheet( hospital: Hospital ){
        viewModelScope.launch {
            setBottomSheetLoading(true)
            delay(2500L)
            detailSheetUiState.value = detailSheetUiState.value.copy(
                isLoading = false,
                hospital.name,
                hospital.availBloods,
                hospital.availPlasma,
                hospital.availPlatelets
            )
            setBottomSheetLoading(false)
        }
    }

    fun addNewMarker(marker: Marker){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            mapUiState = HomeScreenUiState.MapMarkersUiState(homeScreenUiState.value.mapUiState.markers + marker.position)
        )
    }

    fun addMarkerWithInfoWindow(marker:Marker){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            markersWithInfoWindow = homeScreenUiState.value.markersWithInfoWindow + marker
        )
    }

    fun clearInfoWindowMarkerList(){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            markersWithInfoWindow = emptyList()
        )
    }



}