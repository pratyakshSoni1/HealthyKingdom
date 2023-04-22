package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.pratyaksh.healthykingdom.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay

@Composable
fun MapLocationPreview(
    modifier: Modifier,
    location: GeoPoint,
    name: String
){
    val ctx = LocalContext.current
    val mapView = remember{ mutableStateOf( MapView(ctx) ) }
    Box(
        modifier= modifier.clip(RoundedCornerShape(12.dp)),
    ){

        AndroidView(
            factory = { mapView.value },
            modifier=Modifier.matchParentSize()
        ){
            it.setTileSource(TileSourceFactory.MAPNIK)

            val rotationalGestOverlay = RotationGestureOverlay(it)
            it.setMultiTouchControls(true)
            it.overlays.add(rotationalGestOverlay)
            it.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            it.controller.animateTo(location, 15.0,800L )
            it.overlays.add(
                Marker(it).apply {
                    icon = ResourcesCompat.getDrawable( ctx.resources, R.drawable.icmark_hospital, null )
                    title = name
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    position = location
                }
            )

        }

    }


}