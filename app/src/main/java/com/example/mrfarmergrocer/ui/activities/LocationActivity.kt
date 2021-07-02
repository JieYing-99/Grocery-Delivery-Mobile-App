package com.example.mrfarmergrocer.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Transformations.map
import com.example.mrfarmergrocer.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationActivity: AppCompatActivity(),  OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val mfg = LatLng(3.089584608794152, 101.6919275477627)
        mMap.addMarker(MarkerOptions().position(mfg).title("Mr Farmer Grocer"))


        val cameraPosition = CameraPosition.builder().run {
            target(mfg)
            zoom(15f) //1 = world, 5 = city, 15 = street, 20 = building

        }.build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}