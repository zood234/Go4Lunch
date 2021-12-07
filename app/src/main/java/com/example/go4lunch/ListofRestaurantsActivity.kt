package com.example.go4lunch

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.adapters.AllRestaurantsRVAdapter
import com.example.go4lunch.models.nearbysearch.AllItems
import com.example.go4lunch.models.nearbysearch.Restaurants
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_listof_restaurants.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*





// Working uses this to get the restraunts for a specific lat and long   https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.50979902325245,0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc
//Working uses this for search https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=cruise&location=51.50979902325245,%20-0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc
//Working use this to get a specific image https://lh3.googleusercontent.com/places/AAcXr8oleYdQX1u9pF9r8Da9PCU40jZiYbiMKjSHCHwSHnIsnAteYRY81vLVAATKpIyUDqtZKo_QHROaEfOSr90QKnvWEetEsQRBW0Q=s1600-w400
class ListofRestaurantsActivity : AppCompatActivity() {
    private  lateinit var  locationManager: LocationManager
    private lateinit var  locationListener: LocationListener
    private lateinit var viewModel: AllRestaurantsViewModel
    lateinit var alluserRV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_restaurants)
        viewModel = ViewModelProvider(this).get(AllRestaurantsViewModel::class.java)
        val itemAdapter = AllRestaurantsRVAdapter(this, allRest)

        alluserRV= findViewById<RecyclerView>(R.id.allResaurantsRV)
        alluserRV.layoutManager = LinearLayoutManager(this)
        alluserRV.setHasFixedSize(true)

         locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                val currentLocation = LatLng(location.latitude, location.longitude)

                val geocoder = Geocoder(this@ListofRestaurantsActivity, Locale.getDefault())

                try {
                    val addressList =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addressList.size > 0) {
                        println(location.latitude + location.longitude)
                        println(addressList.get(0).toString())
                        ///put the search code in here

                        var b = 51.50979902325245
                        var c = 20 - 0.12624660554017764


                            viewModel.makeApiNearbyCall(location.latitude.toString() + "," + location.longitude.toString())

                            recyclerView(applicationContext, viewModel.makeApiNearbyCall(location.latitude.toString() + "," + location.longitude.toString()))
                        }


                } catch (e: Exception) {
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

            }
        }

        viewModel.liveDataRestaurants.observe(this, androidx.lifecycle.Observer{
            button.text = it.addressList
            recyclerView(applicationContext, allRest)
        })


button.setOnClickListener {
    recyclerView(applicationContext, allRest)

}






    }


    //Might need this to request permision
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 1) {
//            if(grantResults.size < 1) {
//                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//    val listener = object : GoogleMap.OnMapClickListener {
//        override fun onMapClick(p0: LatLng) {
//            val geocoder = Geocoder(this@ListofRestaurantsActivity, Locale.getDefault())
//            if (p0 != null) {
//                var adress = ""
//                try {
//                    val adressList = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
//                    if (adressList.size > 0) {
//                        if (adressList.get(0).thoroughfare != null) {
//                            adress += adressList.get(0).thoroughfare
//                            if (adressList.get(0).subThoroughfare != null) {
//                                adress += adressList.get(0).subThoroughfare
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }

    fun recyclerView( context:Context,  allRest : List<AllItems>)
    {


        println("Recycler view is now starting")
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = AllRestaurantsRVAdapter(this, allRest, )
        allResaurantsRV.layoutManager = LinearLayoutManager(this)
        allResaurantsRV.adapter = itemAdapter
        //updates the recycler view
      //  delay(10000)
        itemAdapter.notifyDataSetChanged()
    }

    //card_view_all_resaurants_design
    }
