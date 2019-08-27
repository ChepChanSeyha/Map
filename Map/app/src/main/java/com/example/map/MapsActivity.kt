package com.example.map

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.Data.LineResponse
import com.example.myapplication.Data.RetrofitClient
import com.example.myapplication.Data.RouteItem
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val client = RetrofitClient()
    private val call = client.getService().getRouteResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        call.enqueue(object : Callback<LineResponse> {
            override fun onFailure(call: Call<LineResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "Get Status error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<LineResponse>, myResponse: Response<LineResponse>) {
                myResponse.body()?.let {
                    Toast.makeText(this@MapsActivity, "Get Status Success", Toast.LENGTH_LONG).show()
                }

                // Declare polyline object and set up color and width
                val polylineOptions = PolylineOptions()
                polylineOptions.color(Color.RED)
                polylineOptions.width(5f)

                val gg = myResponse.body()?.route!![0].coordinates

                if (gg != null) {
                    for (i in 0 until gg.size) {
                        val lat = gg[i][0]
                        val lng = gg[i][1]
                        polylineOptions.add(LatLng(lat, lng))
                    }
                }

                // Add markers
                val location1 = LatLng(gg!![0][0], gg[0][1])
                val location2 = LatLng(gg[gg.size-1][0], gg[gg.size-1][1])

                val center = LatLng(gg[gg.size/2][0], gg[gg.size/2][1])

                mMap.addMarker(MarkerOptions().position(location1).title("Marker in Location1"))
                mMap.addMarker(MarkerOptions().position(location2).title("Marker in Location2"))

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 13f))
                mMap.addPolyline(polylineOptions)

            }
          })

    }

}
