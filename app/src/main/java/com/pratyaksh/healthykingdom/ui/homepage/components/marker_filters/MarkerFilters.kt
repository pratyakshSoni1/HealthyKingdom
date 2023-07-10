package com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreenFilters(
    filterOptions: List<FilterOption>,
    selectedFilter: MarkerFilters,
    onClick:(MarkerFilters)->Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ) {

            items(filterOptions) {
                FilterOption(
                    isSelected = it.type == selectedFilter, title = it.title, icon= it.icon
                ) { onClick(it.type) }
            }

        }
    }

}

@Composable
private fun FilterOption(
    icon: Int,
    isSelected: Boolean,
    title: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(2.dp)
            .clip(RoundedCornerShape(100.dp))
            .clickable(MutableInteractionSource(), indication = null) {
                onClick()
            }

            .border( width = 2.dp, color= if(isSelected) Color.Transparent else Color.Black, shape =  RoundedCornerShape(100.dp))
            .background(if (isSelected) Color(0xFF007BFF) else Color.White)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .padding(4.dp)
                .defaultMinSize(minWidth = 20.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }

}