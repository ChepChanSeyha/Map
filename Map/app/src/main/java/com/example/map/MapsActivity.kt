package com.example.map

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.Data.LineResponse
import com.example.myapplication.Data.RetrofitClient

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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

                val gg = myResponse.body()?.route!!

                val dataList= gg[0].coordinates

//                Log.d("start", dataList.toString())
//
//                // Add markers
//                val location1 = LatLng(dataList.coordinates!![0][0], dataList.coordinates[0][1])
//                val location2 = LatLng(dataList.coordinates[66][0], dataList.coordinates[66][1])
//
//                mMap.addMarker(MarkerOptions().position(location1).title("Marker in Location1"))
//                mMap.addMarker(MarkerOptions().position(location2).title("Marker in Location2"))

                // Declare polyline object and set up color and width
                val polylineOptions = PolylineOptions()
                polylineOptions.color(Color.RED)
                polylineOptions.width(5f)


//                polylineOptions.add(LatLng(1.0, 1.0))
//                polylineOptions.add(LatLng(1.0, 1.0))


                mMap.addPolyline(polylineOptions)


            }

        })

    }

}
