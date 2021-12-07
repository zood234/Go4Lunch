package com.example.go4lunch

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.go4lunch.activity.RestaurantActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.go4lunch.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.Marker
import java.lang.Exception
import java.util.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener




class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private  lateinit var  locationManager: LocationManager
    private lateinit var  locationListener: LocationListener
    private lateinit var viewModel: AllRestaurantsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AllRestaurantsViewModel::class.java)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.liveDataRestaurants.observe(this, androidx.lifecycle.Observer{

            if (it == true) {

                for(i in 0..viewModel.allRest.size -1){
                    val currentLocation = LatLng(viewModel.allRest[i].latList, viewModel.allRest[i].lngList)

                    mMap.addMarker(MarkerOptions().position(currentLocation).title(viewModel.allRest[i].titleList))

                }


            }
        })


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
                println("marker was presed" + marker.title)
                if (marker.title == "You are here" || marker.title =="Last Know Location"){
                    println("User Clicked last known location")
                }
                else{

                val intent = Intent(this, RestaurantActivity::class.java)
                    intent.putExtra("Name", marker.title)
                startActivity(intent)}

            }
            true
        }




        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{

            override fun onLocationChanged(location: Location) {
                mMap.clear()
                val currentLocation = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(currentLocation).title("You are here"))
                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f))

                viewModel.makeApiNearbyCall(location.latitude.toString() + "," + location.longitude.toString())

                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

                try {
                    val addressList = geocoder.getFromLocation(location.latitude,location.longitude,1)
                    if(addressList.size>0){
                        println(addressList.get(0).toString())
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
        else{
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null){
                val last_knownLatLang = LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude)
                mMap.addMarker(MarkerOptions().position(last_knownLatLang).title("Last Know Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(last_knownLatLang,15f))
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if(grantResults.size < 1) {
                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    val listener = object : GoogleMap.OnMapClickListener {
        override fun onMapClick(p0: LatLng) {
            mMap.clear()
            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
            if (p0 != null) {
                var adress = ""
                try {
                    val adressList = geocoder.getFromLocation(p0.latitude, p0.longitude,1)
                    if (adressList.size > 0 ) {
                        if (adressList.get(0).thoroughfare != null) {
                            adress += adressList.get(0).thoroughfare
                            if (adressList.get(0).subThoroughfare != null) {
                                adress += adressList.get(0).subThoroughfare
                            }
                        }
                    }
                } catch (e:Exception){
                    e.printStackTrace()
                }
                mMap.addMarker(MarkerOptions().position(p0).title(adress))
            }
        }
    }





}