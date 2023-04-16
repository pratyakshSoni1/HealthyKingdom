package com.pratyaksh.healthykingdom.ui.utils

import android.widget.TextView
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.domain.model.Hospital
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class HospitalsCustomWindow(
    private val mapView:MapView,
    val hospital: Hospital
): InfoWindow(R.layout.info_window, mapView) {

    private lateinit var hospitalName: TextView
    private lateinit var hospitalMail: TextView
    private lateinit var hospitalPhone: TextView

    override fun onOpen(item: Any?) {

        mView.apply {
            hospitalName = findViewById(R.id.hospitalNameTxt)
            hospitalMail = findViewById(R.id.hospitalMailTxt)
            hospitalPhone = findViewById(R.id.hospitalPhoneTxt)
        }

        hospital.apply {
            hospitalName.text = name
            hospitalMail.text = mail
            hospitalPhone.text = phone
        }

    }

    override fun onClose() {

    }

}