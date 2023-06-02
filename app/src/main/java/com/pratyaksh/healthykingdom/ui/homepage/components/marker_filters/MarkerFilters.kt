package com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pratyaksh.healthykingdom.utils.LifeFluids

@Composable
fun MarkerFilters(
    filterOptions: List<FilterOption>
) {

    Box(
        modifier= Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ){

            items(filterOptions) {
                FilterOption(isSelected = it.isSelected, title = it.title) { it.onClick() }
            }

        }
    }

}

@Composable
private fun FilterOption(
    isSelected: Boolean,
    title: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(MutableInteractionSource(), indication = null) {
                onClick()
            }
            .background(if (isSelected) Color(0xFF007BFF) else Color.White)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.Black else Color.White,
            modifier = Modifier.defaultMinSize(minWidth = 20.dp),
            textAlign = TextAlign.Center
        )
    }

}