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
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.util.*
import kotlinx.android.synthetic.main.activity_listof_restaurants.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.nav_header.*
import com.google.android.gms.maps.model.Marker





class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private  lateinit var  locationManager: LocationManager
    private lateinit var  locationListener: LocationListener
    private lateinit var viewModel: AllRestaurantsViewModel
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AllRestaurantsViewModel::class.java)
        viewModel.getUserData()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        toggle = ActionBarDrawerToggle(this,drawerLayoutMaps,R.string.open, R.string.close)
//        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navViewMaps.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mYourLunch -> startUserProfile()
                R.id.mSettings -> Toast.makeText(applicationContext,"item 2",Toast.LENGTH_SHORT).show()
                R.id.mLogout -> signOut()

            }
            true
        }


        mapAllUsersBtn.setOnClickListener {
            val intent = Intent(this, AllUsersActivity::class.java)
            startActivity(intent)

        }



        mapListBtn.setOnClickListener {
            val intent = Intent(this, ListofRestaurantsActivity::class.java)
            startActivity(intent)

        }







        viewModel.liveDataRestaurants.observe(this, androidx.lifecycle.Observer{

            if (it == true) {

                for(i in 0..viewModel.allRest.size -1){
                    val currentLocation = LatLng(viewModel.allRest[i].latList, viewModel.allRest[i].lngList)

                    mMap.addMarker(MarkerOptions().position(currentLocation).title(viewModel.allRest[i].idList))

                }


            }
        })


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener { marker ->
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
                println("marker was presed" + marker.title)
                if (marker.title == "You are here" || marker.title =="Last Know Location"){
                    println("User Clicked last known location")
                }
                else{
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
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

                viewModel.makeApiNearbyCall(location.latitude,location.longitude)

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


    //inflates the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.app_bar_menu, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        nameSideBarTV.text = viewModel.userName
        emailSideBarTV2.text = viewModel.email
        profilePicIV.setImageBitmap(viewModel.bitmap)

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    fun startUserProfile(){
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }

    fun signOut() {
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance()

        mAuth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }



}