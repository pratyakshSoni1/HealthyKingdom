package com.pratyaksh.healthykingdom.domain.use_case.share_ambulance_loc

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.data.repositories.remote.FirebaseAmbulanceRepoImpl
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.UpdateAmbulanceLocUseCase
import com.pratyaksh.healthykingdom.domain.use_case.get_ambulance.UpdateAmbulanceUseCase
import com.pratyaksh.healthykingdom.utils.NotificationChannelsId
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.osmdroid.util.GeoPoint
import java.util.concurrent.Executor
import java.util.function.Consumer
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.R)
class ShareAmbulanceLocSerice : Service() {

    val updateLocUseCase: UpdateAmbulanceLocUseCase = UpdateAmbulanceLocUseCase( FirebaseAmbulanceRepoImpl(FirebaseFirestore.getInstance()) )

    private var isGpsAvail = false
    private var isNetworkAvail = false
    private var canGetLoc = false
    private lateinit var userId: String

    private lateinit var locManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    fun getLocation(): GeoPoint{
        locManager = this.applicationContext.getSystemService(Service.LOCATION_SERVICE) as LocationManager

        locManager.getCurrentLocation(
            LocationManager.GPS_PROVIDER,
            CancellationSignal(),
            {
                Log.d("SERVICE", "Hellow Executable")
            },
            {
                Log.d("SERVICE", "Hello consumer: ${it.latitude}, ${it.longitude}")
            }
        )

        val tmpLoc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val loc = "LastKnown: ${tmpLoc?.longitude} - ${tmpLoc?.latitude}"
        Log.d("SERVICE", loc)
        if(tmpLoc == null )
            return GeoPoint(0.0, 0.0)

        return GeoPoint(tmpLoc.latitude, tmpLoc.longitude)

    }

    override fun onCreate() {
        Log.d("SHARE_LOC","onCreate Service")

        HandlerThread("ServiceStartThread", THREAD_PRIORITY_BACKGROUND).apply {
            start()
            myLooper = looper
            serviceHandler = ServiceHandler(looper)

        }
    }

    private var serviceHandler: ServiceHandler? = null
    private var myLooper: Looper? = null

    private inner class ServiceHandler(looper: Looper): Handler(looper){
        override fun handleMessage(msg: Message) {
            var i = 0
            repeat(10){
                Thread.sleep(5000L)
                Log.d("SERVICE", "Running ${i++}")
                runBlocking{
                    updateLocUseCase(userId, getLocation()).last().let{
                        Log.d("SERVICE", "Updation Loc: ${it is Resource.Success}")
                    }
                }
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        userId = intent?.extras?.getString("USER_ID")!!

        val ourNotif = NotificationCompat.Builder(this, NotificationChannelsId.AMBULANCE_LIVE_LOC.name).apply {
            setContentTitle("Ambulance is Live")
            setContentText(" Ambulance is sharing live location on map")
            setSmallIcon(R.drawable.ambulance)
        }.build()

        startForeground(159, ourNotif)
        serviceHandler?.obtainMessage()?.also {
            it.arg1 = startId
            serviceHandler?.sendMessage(it)
            Log.d("SERVICE", "Sent msg")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}