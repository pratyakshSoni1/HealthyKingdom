package com.pratyaksh.healthykingdom.ui.homepage


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.getAvailGroups
import com.pratyaksh.healthykingdom.domain.use_case.getFluidsData.GetFluidsByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetAllHospitalsUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_detail_sheet.MarkerDetailSheetUiState
import com.pratyaksh.healthykingdom.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val getAllHospitalsUseCase: GetAllHospitalsUseCase,
    val getHospitalFluidUseCase: GetFluidsByHospitalUseCase,
    private val saveState: SavedStateHandle
): ViewModel() {

    val homeScreenUiState = mutableStateOf( HomeScreenUiState() )

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

    fun initScreen(userId: String){
        homeScreenUiState.value= homeScreenUiState.value.copy(
            userId= userId
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
        detailSheetUiState.value = detailSheetUiState.value.copy(isLoading)
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
                        detailSheetUiState.value = detailSheetUiState.value.copy(
                            isLoading = false,
                            hospitalName= hospital.name,
                            hospitalId = hospital.userId,
                            availBloodTypes = it.data?.bloods!!.getAvailGroups(),
                            availPlasmaTypes = it.data.plasma.getAvailGroups(),
                            availPlateletsTypes = it.data.platelets.getAvailGroups()
                        )
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



}