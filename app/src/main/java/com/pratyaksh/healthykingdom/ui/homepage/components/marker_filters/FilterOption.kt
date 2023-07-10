package com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters

import androidx.compose.ui.graphics.painter.Painter

data class FilterOption(
    val type: MarkerFilters,
    val title: String,
    val icon: Int
)

enum class MarkerFilters{
    REQUESTS,
    HOSPITALS,
    BLOODS,
    PLASMA,
    PLATELETS
}
