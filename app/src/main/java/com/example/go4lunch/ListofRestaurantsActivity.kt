package com.example.go4lunch

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.adapters.AllRestaurantsRVAdapter
import com.example.go4lunch.models.nearbysearch.AllItems
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_listof_restaurants.*
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.nav_header.*
import java.lang.Exception
import java.util.*





// Working uses this to get the restraunts for a specific lat and long   https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.50979902325245,0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc
//Working uses this for search https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=cruise&location=51.50979902325245,%20-0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc
//Working use this to get a specific image https://lh3.googleusercontent.com/places/AAcXr8oleYdQX1u9pF9r8Da9PCU40jZiYbiMKjSHCHwSHnIsnAteYRY81vLVAATKpIyUDqtZKo_QHROaEfOSr90QKnvWEetEsQRBW0Q=s1600-w400
class ListofRestaurantsActivity : AppCompatActivity() {
    private  lateinit var  locationManager: LocationManager
    private lateinit var  locationListener: LocationListener
    private lateinit var viewModel: AllRestaurantsViewModel
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_restaurants)
      toggle = ActionBarDrawerToggle(this,drawerLayoutRest,R.string.open, R.string.close)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(AllRestaurantsViewModel::class.java)

        navViewRest.setNavigationItemSelectedListener {



            when(it.itemId){
                R.id.mYourLunch -> startUserProfile()
                R.id.mSettings -> Toast.makeText(applicationContext,"item 2",Toast.LENGTH_SHORT).show()
                R.id.mLogout -> signOut()

            }
            true
        }

        viewModel.getUserData()

        getLocalRestaurants("")


        allRestAllUsersBtn.setOnClickListener {
            val intent = Intent(this, AllUsersActivity::class.java)
            startActivity(intent)

        }


        allRestMapBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }

    }


    fun startUserProfile(){
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }
    fun getLocalRestaurants(search:String){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                val geocoder = Geocoder(this@ListofRestaurantsActivity, Locale.getDefault())

                try {
                    val addressList =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addressList.size > 0) {
                        println(location.latitude + location.longitude)

                        viewModel.makeApiNearbyCall(search,location.latitude, location.longitude)
                        recyclerView(viewModel.makeApiNearbyCall("",location.latitude,location.longitude))

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

            if (it == true) {
                recyclerView(viewModel.allRest)
            }
        })





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
    //inflates the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        val searchItem2 = menu?.findItem(R.id.auto_search_box)


        val typesOfFood: Array<out String> = resources.getStringArray(R.array.food_type)

        if (searchItem2 != null) {
            val searchView2 = searchItem2.actionView as AutoCompleteTextView
            searchView2.width = 500

            ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,typesOfFood).also {
                    adapter -> searchView2.setAdapter(adapter)
                searchView2.setOnItemClickListener { parent, view, position, id ->getLocalRestaurants(searchView2.text.toString())}
            }




        }

        return true

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

    fun recyclerView(allRest: List<AllItems>)
    {
        lateinit var alluserRV : RecyclerView

allResaurantsRV.setAdapter(null)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = AllRestaurantsRVAdapter(this, allRest, )
        allResaurantsRV.layoutManager = LinearLayoutManager(this)
        allResaurantsRV.adapter = itemAdapter

        itemAdapter.notifyDataSetChanged()
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
