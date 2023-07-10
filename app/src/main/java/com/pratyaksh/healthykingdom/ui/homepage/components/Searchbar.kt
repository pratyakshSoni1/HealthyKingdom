package com.pratyaksh.healthykingdom.ui.homepage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.FilterOption
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.HomeScreenFilters
import com.pratyaksh.healthykingdom.ui.homepage.components.marker_filters.MarkerFilters
import com.pratyaksh.healthykingdom.ui.utils.IconButton
import com.pratyaksh.healthykingdom.utils.Routes

@Composable
fun HomeScreenSearchbar(
    toggleMenu:(setVisible: Boolean)->Unit,
    selectedFilter: MarkerFilters,
    filterOptions: List<FilterOption>,
    onToggleFilter:(MarkerFilters)->Unit
) {

    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search hospitals",
                    modifier = Modifier
                        .padding(8.dp),
                    colorFilter = ColorFilter.tint(Color.LightGray)
                )
                Spacer(Modifier.width(8.dp))

                Text(
                    text= "Search Hospitals...",
                    color = Color.LightGray,
                )
                Spacer(Modifier.width(8.dp))

                IconButton(
                    icon = Icons.Default.AccountCircle, onClick = {
                        toggleMenu(true)
                    },
                    backgroundColor = Color(0xFFFF9800),
                    iconColor = Color.White,
                    size = 2.dp
                )

            }
            Spacer(Modifier.height(8.dp))

            HomeScreenFilters(
                filterOptions = filterOptions, selectedFilter = selectedFilter,
                onClick= onToggleFilter
            )
        }
    }

}