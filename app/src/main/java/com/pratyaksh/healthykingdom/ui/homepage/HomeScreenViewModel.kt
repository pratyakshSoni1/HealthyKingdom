package com.pratyaksh.healthykingdom.ui.homepage
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.getAvailGroups
import com.pratyaksh.healthykingdom.domain.model.toHospitalDto
import com.pratyaksh.healthykingdom.domain.use_case.ambulance_live_loc.GetAllOnlineAmbulanceLocUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getFluidsData.GetFluidsByHospitalUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetAllHospitalsUseCase
import com.pratyaksh.healthykingdom.domain.use_case.getHospital.GetHospitalByIdUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_requests.GetAllRequests
import com.pratyaksh.healthykingdom.domain.use_case.get_requests.GetRequestByHospitalUseCase
import com.pratyaksh.healthykingdom.ui.homepage.components.hospital_detail_sheet.MarkerDetailSheetUiState
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.FilterOption
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.MarkerFilters
import com.pratyaksh.healthykingdom.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalMaterialApi::class)
class HomeScreenViewModel @Inject constructor(
    val getAllHospitalsUseCase: GetAllHospitalsUseCase,
    val getHospitalFluidUseCase: GetFluidsByHospitalUseCase,
    val getRequestsUseCase: GetAllRequests,
    val getRequestsByHospitalUseCase: GetRequestByHospitalUseCase,
    val getHospitalByIdUseCase: GetHospitalByIdUseCase,
    val getLiveAmbulance: GetAllOnlineAmbulanceLocUseCase
) : ViewModel() {

    var isInitialized: Boolean = false

    val homeScreenUiState = mutableStateOf(
        HomeScreenUiState(
            selectedFilter = MarkerFilters.HOSPITALS
        )
    )

    var renderMapAgainOnMarkerChange = true
        private set
    fun toggleMapRender(renderAgain: Boolean){
        renderMapAgainOnMarkerChange = renderAgain
    }

    val detailSheetUiState = mutableStateOf(
        MarkerDetailSheetUiState(
            hospitalName = "",
            hospitalId = "",
            availBloodTypes = listOf(),
            availPlasmaTypes = listOf(),
            availPlateletsTypes = listOf(),
        )
    )

    init {
        getAllHospitals()
    }

    fun initScreen(userId: String, filters: List<FilterOption>) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            userId = userId,
            filters = filters
        )
    }

    fun startSyncingAmbulanceLoc(){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            isSyncingAmbulance = true
        )
        viewModelScope.launch{
            while(homeScreenUiState.value.isSyncingAmbulance){
                getLiveAmbulance().last().let {
                    if(it is Resource.Success){
                        homeScreenUiState.value = homeScreenUiState.value.copy(
                            liveAmbulances = it.data ?: emptyList()
                        )
                    }else if( it is Resource.Error || it.data.isNullOrEmpty() ){
                        Log.d("AMBULANCES", "No live ambulances found")
                    }
                }
            }
            delay(1000L * 10L)
        }
    }

    fun toggleError(setVisible: Boolean) {
        homeScreenUiState.value =
            homeScreenUiState.value.copy(isError = setVisible, isLoading = false)
    }

    private fun getAllHospitals() {

        getAllHospitalsUseCase().onEach {
            when (it) {

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
                    Log.d("DEBUG", "Hospitals Loading")
                }

                is Resource.Error -> {
                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        isLoading = false,
                        isError = true
                    )
                }

            }
            Log.d("TIMECHECK", "Request task complete")
        }.launchIn(viewModelScope)

    }

    private fun updateDetailSheetRequest(hospitalId: String) {
        getRequestsByHospitalUseCase(hospitalId).onEach {

            when (it) {
                is Resource.Error -> {
                    setBottomSheetError(true)
                }

                is Resource.Loading -> {
                    setBottomSheetLoading(true)
                }

                is Resource.Success -> {
                    detailSheetUiState.value = detailSheetUiState.value.copy(
                        requests = it.data
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

    fun getAllRequestsAndFilterHospitals() {
        getRequestsUseCase().onEach {
            when (it) {
                is Resource.Error -> {
                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        isLoading = false,
                        isError = true
                    )
                }

                is Resource.Loading -> {
                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        requests = it.data ?: emptyList()
                    )
                    Log.d("MapMarkerLogs", "Requests fetched :\n${it.data}")

                    homeScreenUiState.value = homeScreenUiState.value.copy(
                        hospitals = emptyList()
                    )
                    homeScreenUiState.value.requests.forEach {
                        toggleLoadingScr(true)
                        try {
                            addHospitalToList(getHospital(it.hospitalId))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        toggleLoadingScr(false)
                    }
                    Log.d(
                        "MapMarkerLogs",
                        "Requests fetched & filtered:\n ${homeScreenUiState.value.hospitals}"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setBottomSheetLoading(isLoading: Boolean = true) {
        detailSheetUiState.value =
            detailSheetUiState.value.copy(isLoading = isLoading, isError = false)
    }

    fun setBottomSheetError(isError: Boolean = true) {
        detailSheetUiState.value =
            detailSheetUiState.value.copy(isError = isError, isLoading = false)

    }

    fun setBottomSheet(hospital: Users.Hospital) {
        viewModelScope.launch {
            setBottomSheetLoading(true)
            updateDetailSheetRequest(hospital.userId)
            getHospitalFluidUseCase(hospital.userId).onEach {

                when (it) {
                    is Resource.Error -> setBottomSheetError(true)
                    is Resource.Loading -> setBottomSheetLoading(true)
                    is Resource.Success -> {

                        if (it.data != null) {
                            detailSheetUiState.value = detailSheetUiState.value.copy(
                                hospitalName = hospital.name,
                                hospitalId = hospital.userId,
                                availBloodTypes = it.data.bloods.getAvailGroups(),
                                availPlasmaTypes = it.data.plasma.getAvailGroups(),
                                availPlateletsTypes = it.data.platelets.getAvailGroups()
                            )
                            Log.d("DEBUG", "SET FLUIDS OF ${hospital.userId}")
                        } else {
                            detailSheetUiState.value = detailSheetUiState.value.copy(
                                hospitalName = hospital.name,
                                hospitalId = hospital.userId,
                                availBloodTypes = emptyList(),
                                availPlasmaTypes = emptyList(),
                                availPlateletsTypes = emptyList()
                            )
                            Log.d("DEBUG", "FLUIDS GOT NULL")
                        }
                        setBottomSheetLoading(false)
                    }
                }
                Log.d("TIMECHECK", "FLUID TASK COMPLETE")
            }.launchIn(viewModelScope)
        }
    }

    fun addNewMarker(marker: Marker) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            mapUiState = HomeScreenUiState.MapMarkersUiState(homeScreenUiState.value.mapUiState.markers + marker)
        )
    }

    fun removeMarker(marker: Marker) {

        val pos = homeScreenUiState.value.mapUiState.markers.indexOf(marker)
        val listSize = homeScreenUiState.value.mapUiState.markers.size

        val leftList =
            homeScreenUiState.value.mapUiState.markers.subList(0, if (pos > 0) pos - 1 else 0)

        val rightList =
            if (pos > listSize || homeScreenUiState.value.mapUiState.markers.isEmpty() || listSize <= 1)
                emptyList()
            else
                homeScreenUiState.value.mapUiState.markers.subList(
                    pos + 1,
                    homeScreenUiState.value.mapUiState.markers.size - 1
                )

        homeScreenUiState.value = homeScreenUiState.value.copy(
            mapUiState = HomeScreenUiState.MapMarkersUiState(leftList + rightList)
        )
    }

    fun addMarkerWithInfoWindow(marker: Marker) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            markersWithInfoWindow = homeScreenUiState.value.markersWithInfoWindow + marker
        )
    }

    fun clearInfoWindowMarkerList() {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            markersWithInfoWindow = emptyList()
        )
    }

    fun toggleMainMenu(setVisible: Boolean? = null) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            isMainMenuVisible = setVisible ?: !homeScreenUiState.value.isMainMenuVisible
        )
    }

    fun toggleLoadingScr(setVisible: Boolean) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            isLoading = setVisible
        )
    }

    suspend fun filterHospitalsByAvailFluid(fluidType: LifeFluids? = null) {

        toggleLoadingScr(true)
        val reqHospitals = mutableListOf<Users.Hospital>()
        clearHospitalList()
        Log.d("DEBUG", "Current hospitals ${homeScreenUiState.value.hospitals.size}")
        getAllHospitalsUseCase().last().let {
            if (it is Resource.Success) {
                homeScreenUiState.value = homeScreenUiState.value.copy(
                    hospitals = it.data ?: emptyList()
                )
            } else {
                Log.d("DEBUG", "Found ${it.msg}")
                toggleError(true)
                toggleLoadingScr(false)
            }
        }

        Log.d(
            "DEBUG",
            "Got the hospitals loading finished ${homeScreenUiState.value.hospitals.size}"
        )
        for (hospital in homeScreenUiState.value.hospitals) {
            Log.d("DEBUG", "Getting fluids from: ${hospital.userId}")
            if(hospital.userId.isNullOrEmpty()) {
                Log.d("DEBUG","Id found blank")
                continue
            }
            getHospitalFluidUseCase(hospital.userId).last().let {
                Log.d("DEBUG", "Got fluids")
                when (it) {
                    is Resource.Error -> {
                        toggleError(true)
                        toggleLoadingScr(false)
                    }

                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        Log.d("DEBUG", "Filtered the hospitals finished")
                        when (fluidType) {
                            LifeFluids.PLASMA -> {
                                if (it.data!!.plasma.getAvailGroups().isNotEmpty())
                                    reqHospitals.add(hospital)
                            }

                            LifeFluids.BLOOD -> {
                                if (it.data!!.bloods.getAvailGroups().isNotEmpty())
                                    reqHospitals.add(hospital)
                            }

                            LifeFluids.PLATELETS -> {
                                if (it.data!!.platelets.getAvailGroups().isNotEmpty())
                                    reqHospitals.add(hospital)
                            }

                            else -> reqHospitals.add(hospital)
                        }
                        homeScreenUiState.value = homeScreenUiState.value.copy(
                            hospitals = reqHospitals
                        )
                    }
                }
            }
        }

        toggleLoadingScr(false)
        Log.d("DEBUG", "ReqHospitals: ${reqHospitals.size}, added: ${homeScreenUiState.value.hospitals.size}")
    }

    fun applyFilter(
        filterType: MarkerFilters
    ) = viewModelScope.launch {
        when (filterType) {
            MarkerFilters.HOSPITALS -> {
                filterHospitalsByAvailFluid(null)
            }

            MarkerFilters.BLOODS -> {
                filterHospitalsByAvailFluid(LifeFluids.BLOOD)
            }

            MarkerFilters.PLASMA -> {
                filterHospitalsByAvailFluid(LifeFluids.PLASMA)
            }

            MarkerFilters.PLATELETS -> {
                filterHospitalsByAvailFluid(LifeFluids.PLATELETS)
            }

            MarkerFilters.REQUESTS -> {
                getAllRequestsAndFilterHospitals()
            }
        }
    }

    fun toggleFilter(newFilter: MarkerFilters){
        homeScreenUiState.value = homeScreenUiState.value.copy(
            selectedFilter = newFilter
        )
    }

    fun addHospitalToList(hospital: Users.Hospital) {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            hospitals = homeScreenUiState.value.hospitals + hospital
        )
    }

    fun clearHospitalList() {
        homeScreenUiState.value = homeScreenUiState.value.copy(
            hospitals = emptyList()
        )
    }

    fun getHospital( hospitalId:String ): Users.Hospital{
        var searchRes: Users.Hospital? = null
        searchRes = homeScreenUiState.value.hospitals.find {
            it.userId == hospitalId
        }
        if(searchRes!=null) return searchRes
        else{
            runBlocking {
                getHospitalByIdUseCase(hospitalId).last().let {
                    if(it is Resource.Success && it.data != null){
                        searchRes = it.data
                    }else{
                        toggleError(true)
                    }
                }
            }
            return searchRes!!
        }
    }

    fun getHospNameAndLocForReqMarker(hospitalId: String): Users.Hospital {
        return homeScreenUiState.value.hospitals.find {
            it.userId == hospitalId
        }!!
    }

}
