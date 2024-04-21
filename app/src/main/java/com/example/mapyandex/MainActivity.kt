package com.example.mapyandex

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.traffic.TrafficLayer
import com.yandex.runtime.image.ImageProvider


class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var trafficbutton:Button

    private val placemarkTapListener = MapObjectTapListener { _, point ->
        Toast.makeText(
            this@MainActivity,
            "Tapped the point (${point.longitude}, ${point.latitude})",
            Toast.LENGTH_SHORT
        ).show()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("cf392e39-1083-4b44-9db4-56b272957353")

        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapview)
        trafficbutton = findViewById(R.id.trafficbutton)

        val map =  mapView.mapWindow.map
        map.move(POSITION)

        val mapKit: MapKit = MapKitFactory.getInstance()
        val probki = mapKit.createTrafficLayer(mapView.mapWindow)

        trafficbutton.setOnClickListener {trafficDisplay(probki)}

        val imageProvider = ImageProvider.fromResource(this, R.drawable.location)
        val placemark = map.mapObjects.addPlacemark().apply {
            geometry = POINT
            setIcon(imageProvider)
        }
            placemark.addTapListener(placemarkTapListener)
        }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    companion object {
        private val POINT = Point(55.751280, 37.629720)
        private val POSITION = CameraPosition(POINT, 17.0f, 150.0f, 30.0f)
    }

    private var probkiison = false
    private fun trafficDisplay(probki: TrafficLayer) {
        when (probkiison){
            false ->{
                probkiison = true
                probki.isTrafficVisible = true
            }
            true -> {
                probkiison = false
                probki.isTrafficVisible = false
            }
        }
    }
}