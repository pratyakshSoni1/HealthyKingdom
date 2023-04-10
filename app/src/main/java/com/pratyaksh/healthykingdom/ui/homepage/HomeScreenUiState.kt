package com.pratyaksh.healthykingdom.ui.homepage

import com.pratyaksh.healthykingdom.domain.model.Hospital
import org.osmdroid.util.GeoPoint

data class HomeScreenUiState(

    val searchText: String= "",
    val hospitals: List<Hospital> = emptyList(),
    val mapUiState: MapMarkersUiState = MapMarkersUiState( emptyList() ),
    val mapActionButtonsUiState: MapActionButtonsUiState = MapActionButtonsUiState(
        true, false
    )

){

    data class MapMarkersUiState(
        val markers: List<GeoPoint>
    )

    data class MapActionButtonsUiState(
        val areHospitalsVisible: Boolean,
        val areOnlyHospitalsVisible: Boolean
    )

}
