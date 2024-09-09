package com.example.week7maps

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.common.MapboxOptions
import com.mapbox.common.location.AccuracyLevel
import com.mapbox.common.location.DeviceLocationProvider
import com.mapbox.common.location.IntervalSettings
import com.mapbox.common.location.Location
import com.mapbox.common.location.LocationObserver
import com.mapbox.common.location.LocationProviderRequest
import com.mapbox.common.location.LocationService
import com.mapbox.common.location.LocationServiceFactory
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.location

const val TAG = "week7Debug"

class MainActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    lateinit var permissionsManager:PermissionsManager

    val locationService : LocationService = LocationServiceFactory.getOrCreate()
    var locationProvider: DeviceLocationProvider? = null

    val locationObserver = object: LocationObserver {
        override fun onLocationUpdateReceived(locations: MutableList<Location>) {
            Log.e(TAG, "Location update received: " + locations)
        }
    }

    var permissionsListener: PermissionsListener = object : PermissionsListener {
        override fun onExplanationNeeded(permissionsToExplain: List<String>) {

        }

        override fun onPermissionResult(granted: Boolean) {
            if (granted) {

                // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location

            } else {

                // User denied the permission

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

       MapboxOptions.accessToken = "pk.eyJ1IjoibWtnYXRsZSIsImEiOiJjbHlpaWNtZXkwYzZzMmlzZWxzNjA0Y2l4In0.R3WiMQ6K-UjHAuzgtLj35g"
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location

            mapView.mapboxMap.setCamera(
                CameraOptions.Builder()
                    .center(Point.fromLngLat(-98.0, 39.5))
                    .pitch(0.0)
                    .zoom(2.0)
                    .bearing(0.0)
                    .build()
            )


            mapView.location.enabled =true


            val request = LocationProviderRequest.Builder()
                .interval(IntervalSettings.Builder().interval(0L).minimumInterval(0L).maximumInterval(0L).build())
                .displacement(0F)
                .accuracy(AccuracyLevel.HIGHEST)
                .build();



            val result = locationService.getDeviceLocationProvider(request)
            if (result.isValue) {
                locationProvider = result.value!!
            } else {
                Log.e(TAG,"Failed to get device location provider")
            }

            locationProvider?.addLocationObserver(locationObserver)

        } else {
            permissionsManager = PermissionsManager(permissionsListener)
            permissionsManager.requestLocationPermissions(this)
        }




    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode,
            permissions as Array<String>, grantResults)
    }
}