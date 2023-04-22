package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.pratyaksh.healthykingdom.R
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun LocationChooserDialog(
    onSelectLocation:(location: GeoPoint) -> Unit,
    onCancel:()->Unit
){
    val context = LocalContext.current
    val mapView = remember { mutableStateOf(MapView(context)) }
    val marker = remember{ mutableStateOf(
        Marker(mapView.value).apply {
            icon = ResourcesCompat.getDrawable( context.resources, R.drawable.icmark_hospital, null )
            title = "Select this location"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            position = GeoPoint(0.0, 0.0)
        }
    ) }

    var location: GeoPoint = GeoPoint(0.0, 0.0)

    val lifeCycleOwner = LocalLifecycleOwner.current
    val observer =  LifecycleEventObserver{ _, event ->
        when(event){
            Lifecycle.Event.ON_PAUSE -> { mapView.value.onPause() }
            Lifecycle.Event.ON_RESUME -> { mapView.value.onResume() }
            else -> Unit
        }
    }

    DisposableEffect(key1 = Unit) {
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier= Modifier
            .fillMaxSize()
            .background(Color(0x32000000)),
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier= Modifier
                .fillMaxSize(0.8f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(8.dp)
        ){

            Box(
                modifier= Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .clip(RoundedCornerShape(8.dp))
            ){
                AndroidView(
                    factory = { mapView.value },
                    modifier= Modifier.matchParentSize()
                ){
                    it.setTileSource(TileSourceFactory.MAPNIK)
                    it.controller.zoomTo(5, 500L)

                    val rotationalGestOverlay = RotationGestureOverlay(it)
                    it.setMultiTouchControls(true)
                    it.overlays.add(rotationalGestOverlay)
                    it.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                    it.overlays.add(marker.value)

                    val clickListeners = MapEventsOverlay(object: MapEventsReceiver {
                        override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
                            marker.value.position = geoPoint
                            location = geoPoint!!
                            return true
                        }

                        override fun longPressHelper(p: GeoPoint?): Boolean {
                            return false
                        }
                    })
                    it.overlays.add(clickListeners)

                }

            }

            Button(
                onClick = {

                    val myLocationOverlay = MyLocationNewOverlay( GpsMyLocationProvider(context) ,mapView.value)
                    myLocationOverlay.enableMyLocation()

                    mapView.value.apply {
                        overlays.add(myLocationOverlay)
                        controller.zoomTo(15, 500L)
                        location = myLocationOverlay.myLocation ?: GeoPoint(10.0,10.0)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Fetch Current")
            }

            Button(
                onClick = {
                    onSelectLocation( location )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Select")
            }

        }

    }

}