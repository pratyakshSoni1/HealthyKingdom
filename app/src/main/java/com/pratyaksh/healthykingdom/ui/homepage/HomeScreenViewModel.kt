package com.pratyaksh.healthykingdom.ui.homepage


import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.getAvailGroups
import com.pratyaksh.healthykingdom.domain.use_case.getFluidsData.GetFluidsByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetAllHospitalsUseCase
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet.MarkerDetailSheetUiState
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.FilterOption
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.MarkerFilters
import com.pratyaksh.healthykingdom.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalMaterialApi::class)
class HomeScreenViewModel @Inject constructor(
    val getAllHospitalsUseCase: GetAllHospitalsUseCase,
    val getHospitalFluidUseCase: GetFluidsByHospitalUseCase
): ViewModel() {

    val homeScreenUiState = mutableStateOf( HomeScreenUiState(
        selectedFilter = MarkerFilters.HOSPITALS
    ) )

    val detailSheetUiState = mutableStateOf( MarkerDetailSheetUiState(
        hospitalName = "",
        hospitalId = "",
        availBloodTypes = listOf(),
        availPlasmaTypes = listOf(),
        availPlateletsTypes = listOf(),
    ) )

    init {
        getAllHospitals()
    }

    fun initScreen(userId: String, filters: List<FilterOption>){
        homeScreenUiState.value= homeScreenUiState.value.copy(
            userId= userId,
            filters = filters
        )
    }

    fun toggleError(setVisible: Boolean){
        homeScreenUiState.value = homeScreenUiState.value.copy(isError= setVisible, isLoading = false)
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
        detailSheetUiState.value = detailSheetUiState.value.copy(isLoading = isLoading, isError = false)
    }
    fun setBottomSheetError( isError:Boolean = true ){
        detailSheetUiState.value = detailSheetUiState.value.copy(isError = isError, isLoading = false)

    }
    fun setBottomSheet( hospital: Users.Hospital ){
        viewModelScope.launch {
            setBottomSheetLoading(true)
            getHospitalFluidUseCase(hospital.userId).collectLatest {

                when(it){
                    is Resource.Error -> setBottomSheetError(true)
                    is Resource.Loading -> setBottomSheetLoading(true)
                    is Resource.Success -> {

                        if( it.data != null ){
                            detailSheetUiState.value = detailSheetUiState.value.copy(
                                isLoading = false,
                                hospitalName= hospital.name,
                                hospitalId = hospital.userId,
                                availBloodTypes = it.data.bloods.getAvailGroups(),
                                availPlasmaTypes = it.data.plasma.getAvailGroups(),
                                availPlateletsTypes = it.data.platelets.getAvailGroups()
                            )
                        }else{
                            detailSheetUiState.value = detailSheetUiState.value.copy(
                                isLoading = false,
                                hospitalName= hospital.name,
                                hospitalId = hospital.userId,
                                availBloodTypes = emptyList(),
                                availPlasmaTypes = emptyList(),
                                availPlateletsTypes = emptyList()
                            )
                        }
                        setBottomSheetLoading(false)
                    }
                }

            }
            setBottomSheetLoading(false)
        }
    }

    fun addNewMarker(marker: Marker){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            mapUiState = HomeScreenUiState.MapMarkersUiState(homeScreenUiState.value.mapUiState.markers + marker.position)
        )
    }
    fun removeMarker(marker: GeoPoint){

        val pos = homeScreenUiState.value.mapUiState.markers.indexOf(marker)

        val leftList = homeScreenUiState.value.mapUiState.markers.subList(0, pos-1)
        val rightList = homeScreenUiState.value.mapUiState.markers.subList(pos+1, homeScreenUiState.value.mapUiState.markers.size-1)

        homeScreenUiState.value = homeScreenUiState.value.copy(
            mapUiState = HomeScreenUiState.MapMarkersUiState(leftList + rightList )
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

    fun toggleMainMenu(setVisible: Boolean? = null ){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            isMainMenuVisible = setVisible ?: !homeScreenUiState.value.isMainMenuVisible
        )
    }

    fun toggleLoadingScr(setVisible: Boolean){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            isLoading = setVisible
        )
    }

    fun applyFilter(
        filterType: MarkerFilters
    ){

        when(filterType){
            MarkerFilters.HOSPITALS -> TODO()
            MarkerFilters.BLOODS -> TODO()
            MarkerFilters.PLASMA -> TODO()
            MarkerFilters.PLATELETS -> TODO()
            MarkerFilters.REQUESTS -> TODO()
        }

    }

}