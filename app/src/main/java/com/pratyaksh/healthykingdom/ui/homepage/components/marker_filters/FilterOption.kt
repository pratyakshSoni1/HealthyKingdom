package com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters

data class FilterOption(
    val type: MarkerFilters,
    val title: String,
    val onClick:()->Unit,
    val isSelected: Boolean
)

enum class MarkerFilters{
    HOSPITALS,
    BLOODS,
    PLASMA,
    PLATELETS,
    REQUESTS
}
