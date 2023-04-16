package com.pratyaksh.healthykingdom.ui.homepage

import com.pratyaksh.healthykingdom.domain.model.Hospital
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

data class HomeScreenUiState(

    val searchText: String= "",
    val hospitals: List<Hospital> = emptyList(),
    val mapUiState: MapMarkersUiState = MapMarkersUiState( emptyList() ),
    val markersWithInfoWindow: List<Marker> = emptyList(),
    val mapActionButtonsUiState: MapActionButtonsUiState = MapActionButtonsUiState(true, false),
    val isLoading: Boolean = false,
    val isError: Boolean = false

){

    data class MapMarkersUiState(
        val markers: List<GeoPoint>
    )

    data class MapActionButtonsUiState(
        val areHospitalsVisible: Boolean,
        val areOnlyHospitalsVisible: Boolean
    )

}
