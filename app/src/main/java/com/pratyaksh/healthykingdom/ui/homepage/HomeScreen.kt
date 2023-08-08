package com.pratyaksh.healthykingdom.ui.homepage

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.ui.homepage.components.HomeScreenDialogMenu
import com.pratyaksh.healthykingdom.ui.homepage.components.HomeScreenSearchbar
import com.pratyaksh.healthykingdom.ui.homepage.components.HospitalsCustomWindow
import com.pratyaksh.healthykingdom.ui.homepage.components.MapActionButtons
import com.pratyaksh.healthykingdom.ui.homepage.components.MapComponent
import com.pratyaksh.healthykingdom.ui.homepage.components.hospital_detail_sheet.HospitalDetailsSheet
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.FilterOption
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.MarkerFilters
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import com.pratyaksh.healthykingdom.utils.identifyUserTypeFromId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavHostController,
    logoutUser: () -> Flow<Resource<Boolean>>,
    getLoggedUser: () -> Flow<Resource<String?>>
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()


    val sheetState: BottomSheetState =
        rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    var sheetPeekState: Dp by remember { mutableStateOf(0.dp) }
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val mapView = remember {
        mutableStateOf(MapView(context))
    }


    fun toggleSheetState(setCollapse: Boolean) {
        coroutine.launch {
            if (setCollapse) sheetState.collapse() else sheetState.expand()
        }
    }

    fun toggleSheetPeek(setVisible: Boolean) {
        sheetPeekState = if (setVisible) 90.dp else 0.dp
    }

    BackHandler {
        if (sheetState.isExpanded) {
            toggleSheetState(true)
        } else if (sheetPeekState > 0.dp) {
            toggleSheetPeek(true)
        }
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.startSyncingAmbulanceLoc()
    })

    LaunchedEffect(key1 = viewModel.homeScreenUiState.value.liveAmbulances.size, block = {
        for( ambulances in viewModel.homeScreenUiState.value.mapUiState.ambulanceMarkers){
            mapView.value.overlays.remove(ambulances)
            viewModel.removeLiveAmbulanceMarker(ambulances)
        }
        mapView.value.invalidate()

        for (ambulance in viewModel.homeScreenUiState.value.liveAmbulances){
            mapView.value.addAmbulanceToMap( ambulance ){
                closeAllInfoWindow(viewModel)
            }.let{
                viewModel.addNewAmbulanceMarker(it)
                viewModel.addMarkerWithInfoWindow( it )
            }
        }
    })

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName

        getLoggedUser().collectLatest {
            when (it) {
                is Resource.Error -> viewModel.toggleError(true)
                is Resource.Loading -> viewModel.toggleLoadingScr(true)
                is Resource.Success -> {
                    if (it.data != null) {
                        viewModel.initScreen(it.data, getFilters())
                        viewModel.toggleLoadingScr(false)
                    } else {
                        viewModel.toggleError(true)
                        delay(2000L)
                        navController.navigate(Routes.SIGNUP_NAVGRAPH.route) {
                            popUpTo(Routes.HOME_NAVGRAPH.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(
        key1 = viewModel.homeScreenUiState.value.selectedFilter,
        key2 = viewModel.homeScreenUiState.value.requests.size,
        key3 = viewModel.homeScreenUiState.value.hospitals.size,
        block = {
            Log.d("DEBUG", "In LaunchEffect")
            if( viewModel.renderMapAgainOnMarkerChange ){
                Log.d("DEBUG", "Apply mapFilter Effect")
                mapView.value.applyFilterToMap(
                    viewModel = viewModel,
                    onToggleSheetState = { toggleSheetState( it ) }
                )
                Log.d("DEBUG", "Added markers: ${viewModel.homeScreenUiState.value.mapUiState.markers.size}")
            }else {
                Log.d("DEBUG", "SKIP mapFilter Effect")
            }
        })

    if (!viewModel.homeScreenUiState.value.isError) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                HospitalDetailsSheet(
                    uiState = viewModel.detailSheetUiState.value,
                    onCloseClick = {
                        toggleSheetPeek(false)
                        toggleSheetState(true)
                        closeAllInfoWindow(viewModel)
                    },
                    onDetailsClick = {
                        viewModel.toggleLoadingScr(false)
                        toggleSheetState(true)
                        Log.d("DEBUG", "Nav to ${Routes.HOSPITAL_DETAILS_SCREEN.route + "/$it"}")
                        navController.navigate(Routes.HOSPITAL_DETAILS_SCREEN.route + "/$it")
                    }
                )
            },
            sheetPeekHeight = sheetPeekState,
            sheetElevation = 12.dp,
            sheetBackgroundColor = Color.Transparent,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MapComponent(mapView) {
                    toggleSheetState(true)
                    toggleSheetPeek(viewModel.homeScreenUiState.value.markersWithInfoWindow.isNotEmpty())
                }
                MapActionButtons(
                    onToggleAmbulances = { isVisible ->
                        viewModel.updateAmbulancesVisibility(isVisible)
                        if(isVisible){
                             if(!viewModel.homeScreenUiState.value.isSyncingAmbulance)
                                 viewModel.startSyncingAmbulanceLoc()
                         }else{
                             Log.d("DEBUG", "AmbulanceVisiblity: $isVisible")
                             viewModel.stopSyncingAmbulanceLoc()
                             viewModel.removeAllLiveAmbulance()
                         }
                    },
                    isAmbulancesVisble = viewModel.homeScreenUiState.value.mapUiState.isAmbulancesVisible
                )
                HomeScreenSearchbar(
                    toggleMenu = {
                        if (sheetState.isExpanded) {
                            toggleSheetState(true)
                            toggleSheetPeek(true)
                        }
                        viewModel.toggleMainMenu(it)
                    },
                    filterOptions = viewModel.homeScreenUiState.value.filters,
                    selectedFilter = viewModel.homeScreenUiState.value.selectedFilter,
                    onToggleFilter = {newFilter ->
                        viewModel.toggleMapRender(false)
                        viewModel.applyFilter(newFilter).invokeOnCompletion { _ ->
                            Log.d("DEBUG","ViewModel filtered data")
                            viewModel.toggleMapRender(true)
                            viewModel.toggleFilter(newFilter)
                            Log.d("DEBUG","In Invoke Completion")
                        }
                    }
                )

                if (viewModel.homeScreenUiState.value.isMainMenuVisible) {
                    HomeScreenDialogMenu(
                        userName = "User Name",
                        navController = navController,
                        onLogout = {
                            viewModel.toggleLoadingScr(true)
                            CoroutineScope(Dispatchers.IO).launch {
                                if(viewModel.updateUserLogoutToFB()){
                                    logoutUser().collectLatest {
                                        if (it.data == true) {
                                            viewModel.deleteSettingsDb()
                                            withContext(Dispatchers.Main) {
                                                viewModel.toggleLoadingScr(false)
                                                navController.navigate(Routes.SIGNUP_NAVGRAPH.route) {
                                                    popUpTo(Routes.HOME_NAVGRAPH.route) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        } else {
                                            viewModel.toggleLoadingScr(false)
                                        }
                                    }
                                }else{
                                    Toast.makeText(context, "Can't logout, try agin later", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        onCloseMenu = { viewModel.toggleMainMenu(false) },
                        userId = viewModel.homeScreenUiState.value.userId!!
                    )
                }
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {

                }

                if (viewModel.homeScreenUiState.value.isLoading) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color(0x2D000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingComponent(
                            modifier = Modifier
                                .fillMaxSize(0.35f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                        )
                    }
                }
            }
        }
    } else {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Unexpected Error :(\n Comeback Later",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }


}

fun closeAllInfoWindow(viewModel: HomeScreenViewModel) {
    viewModel.apply {
        homeScreenUiState.value.markersWithInfoWindow.forEach {
            it.closeInfoWindow()
        }
        viewModel.clearInfoWindowMarkerList()
    }
}

fun MapView.addHospitalToMap(
    hospital: Users.Hospital,
    filter: MarkerFilters,
    onMarkerClick: (marker: Marker) -> Unit,
): Marker {

    Log.d("MarkerLogs", "Start Adding ${hospital.name}")
    val newMarker = Marker(this).apply {
        position = hospital.location
        icon = ResourcesCompat.getDrawable(
            context.resources,
            when (filter) {
                MarkerFilters.REQUESTS -> R.drawable.icmark_request
                MarkerFilters.HOSPITALS -> R.drawable.icmark_hospital
                MarkerFilters.BLOODS -> R.drawable.icmark_blood
                MarkerFilters.PLASMA -> R.drawable.icmark_plasma
                MarkerFilters.PLATELETS -> R.drawable.icmark_platelets
            },
            null
        )
        title = hospital.name
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        subDescription = "This is ${hospital.name} location on the map"
        id = hospital.userId
        Log.d("MarkerLogs", "Adding ${hospital.name}")
        infoWindow = HospitalsCustomWindow(this@addHospitalToMap, hospital)
        Log.d("DEBUG", "Added info window")
        setOnMarkerClickListener { marker, mapView ->
            onMarkerClick(marker)
            showInfoWindow()
            true
        }
        Log.d("DEBUG", "Added listener")
    }

    overlays.add(newMarker)
    Log.d("DEBUG", "Added overlay")
    invalidate()
    return newMarker

}


fun MapView.addAmbulanceToMap(
    ambulance: Users.Ambulance,
    onMarkerClick: (marker: Marker) -> Unit,
): Marker {

    Log.d("MarkerLogs", "Start Adding ${ambulance.driverName}")
    val newMarker = Marker(this).apply {
        position = ambulance.vehicleLocation
        icon = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.icmark_ambulance,
            null
        )
        title = ambulance.driverName
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        subDescription = "This is ${ambulance.driverName} location on the map"
        id = ambulance.userId
        Log.d("MarkerLogs", "Adding ${ambulance.driverName}")
        setOnMarkerClickListener { marker, mapView ->
            onMarkerClick(marker)
            true
        }
        Log.d("DEBUG", "Added listener")
    }

    overlays.add(newMarker)
    Log.d("DEBUG", "Added overlay")
    invalidate()
    return newMarker

}



fun MapView.addRequestsToMap(
    request: Requests,
    onMarkerClick: (marker: Marker) -> Unit,
    getHospitalNameAndLoc: (id: String) -> Users.Hospital
): Marker {

        val hospital = getHospitalNameAndLoc(request.hospitalId)
        val newMarker = Marker(this).apply {
            position = hospital.location
            icon = ResourcesCompat.getDrawable(context.resources, R.drawable.icmark_request, null)
            title = hospital.name
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            subDescription = "This is ${hospital.name} location on the map"
            id = hospital.userId
            Log.d("MarkerLogs", "Adding ${hospital.name}")
            infoWindow = HospitalsCustomWindow(this@addRequestsToMap, hospital)
            this.setOnMarkerClickListener { marker, mapView ->
                onMarkerClick(marker)
                showInfoWindow()
                true
            }
        }

        overlays.add(newMarker)
        invalidate()
        return newMarker
}

private fun getFilters(): List<FilterOption> {

    val filters = mutableListOf<FilterOption>()

    for (item in MarkerFilters.values()) {

        filters.add(
            when (item) {
                MarkerFilters.HOSPITALS -> FilterOption(
                    type = item,
                    title = "Hospitals",
                    icon = R.drawable.hospital
                )

                MarkerFilters.BLOODS -> FilterOption(
                    type = item,
                    title = "Bloods",
                    icon = R.drawable.ic_blood
                )

                MarkerFilters.PLASMA -> FilterOption(
                    type = item,
                    title = "PLASMA",
                    icon = R.drawable.ic_plasma
                )

                MarkerFilters.PLATELETS -> FilterOption(
                    type = item,
                    title = "Platelets",
                    icon = R.drawable.ic_platelets
                )

                MarkerFilters.REQUESTS -> FilterOption(
                    type = item,
                    title = "Requests",
                    icon = R.drawable.requests
                )
            }
        )
    }

    return filters
}

private fun MapView.applyFilterToMap(
    viewModel: HomeScreenViewModel,
    onToggleSheetState:(Boolean)->Unit
){
    val selectedFilter = viewModel.homeScreenUiState.value.selectedFilter
    viewModel.homeScreenUiState.value.mapUiState.markers.forEach {
        overlays.remove(it)
        viewModel.removeMarker(it)
    }
    invalidate()
    Log.d("MapMarkerLogs", "Removed OLD markers")

    Log.d("DEBUG", "TO FILTER: ${selectedFilter}")
    if( selectedFilter == MarkerFilters.REQUESTS ){
        viewModel.clearHospitalList()
        viewModel.homeScreenUiState.value.requests.forEach {
            addRequestsToMap(
                request= it,
                onMarkerClick = { marker ->
                    closeAllInfoWindow(viewModel)
                    viewModel.setBottomSheet(
                        hospital = viewModel.getHospital(it.hospitalId)
                    )
                    onToggleSheetState(false)
                    controller.animateTo(marker.position)
                    viewModel.addMarkerWithInfoWindow(marker = marker)
                },
                getHospitalNameAndLoc = {
                    lateinit var reqHosp: Users.Hospital
                    runBlocking {
                        reqHosp = viewModel.getHospital(it)
                    }
                    reqHosp
                }
            ).let{
                viewModel.addNewMarker(it)
            }
        }

    }else {

        Log.d("DEBUG", "Going to add non-rq markers")
        viewModel.homeScreenUiState.value.hospitals.forEach {
            Log.d("DEBUG", "Going to Add ${it}")
            addHospitalToMap(
                it,
                viewModel.homeScreenUiState.value.selectedFilter
            ) { marker ->
                closeAllInfoWindow(viewModel)
                viewModel.setBottomSheet(hospital = it)
                onToggleSheetState(false)
                controller.animateTo(it.location)
                viewModel.addMarkerWithInfoWindow(marker = marker)
            }.let {
                viewModel.addNewMarker(it)
            }
        }

    }


}

