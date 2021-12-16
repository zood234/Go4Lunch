package com.example.go4lunch

import android.app.Application
import android.content.Intent
import android.location.Location
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.go4lunch.interfaces.NearByRestApi
import com.example.go4lunch.models.nearbysearch.AllItems
import com.example.go4lunch.models.nearbysearch.RestaurantDetails
import com.example.go4lunch.models.nearbysearch.Restaurants
import com.example.harrypottercaracters.RetroInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class AllRestaurantsViewModel(application: Application) : AndroidViewModel(application) {

    var allRest = arrayListOf<AllItems>()
    var restDetails = arrayListOf<RestaurantDetails>()

    var liveDataRestaurants: MutableLiveData<Boolean> = MutableLiveData()
    var liveRestDetails: MutableLiveData<RestaurantDetails> = MutableLiveData()

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.50979902325245,0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc

    fun makeApiNearbyCall(lat: Double,lng:Double): List<AllItems> {
    val latlng = lat.toString()+","+lng.toString()
        var pictureUrl = ""
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)

                var response = retroInstance.getAllNearbyRest(latlng,"5000","restaurant","AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")
                for (i in 0..response.results.size-1) {

                    makeApiPlaceDetails(response.results[i].place_id)

                    try {
                        viewModelScope.launch(Dispatchers.IO) {
                            val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)
                            var responsePlace = retroInstance.getPlaceDetails(response.results[i].place_id,"AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")

                            var time = ""
                            if (responsePlace.result.opening_hours !=null ){
                                 time = responsePlace.result.opening_hours.weekday_text[getCurrentDay()]
                               println("The response for place time " + responsePlace.result.opening_hours.weekday_text[getCurrentDay()])
                            }

                            if (responsePlace.result.photos!= null) {
                                pictureUrl =  getPictureUrl(responsePlace.result.photos[0].photo_reference)
                            }

                            var distance = getDistance(lat,lng,response.results[i].geometry.location.lat,response.results[i].geometry.location.lng)

                            var tempObjectOfResponse = AllItems(response.results[i].name,response.results[i].place_id,pictureUrl,response.results[i].geometry.location.lng
                    ,response.results[i].geometry.location.lat,response.results[i].rating,response.results[i].vicinity,response.results[i].types[0],response.results[i].place_id
                    , time,"1",distance)

                    allRest.add(tempObjectOfResponse)
                            liveDataRestaurants.postValue(true)

                        }
                    } catch (e: IOException) {
                        println("response did not work")
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            println("response did not work")
            e.printStackTrace()

        }
        return allRest
    }


    fun makeApiPlaceDetails(placeID: String) {
        var pictureUrl = ""
        var website = ""
        var phone = ""
        try {

            viewModelScope.launch(Dispatchers.IO) {
                val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)
                var response = retroInstance.getPlaceDetails(placeID,"AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")
                println("The name in the response is " + response.result.name )

                if (response.result.photos!= null) {
                    pictureUrl =  getPictureUrl(response.result.photos[0].photo_reference)
                }

                if (response.result.website!= null) {
                    website =  response.result.website
                }

                if(response.result.international_phone_number!=null){
                    phone = response.result.international_phone_number
                }

                var tempObjectOfResponse = RestaurantDetails(response.result.name,pictureUrl,response.result.formatted_address,
                    response.result.types.toString(),  response.result.user_ratings_total.toString(),phone,website)
                liveRestDetails.postValue(tempObjectOfResponse)



            }
        } catch (e: IOException) {
            println("response did not work")
            e.printStackTrace()

        }


    }

    fun getCurrentDay():Int{
        val calendar = Calendar.getInstance()
        val date = calendar.time
var currentDay = ""
        currentDay = (SimpleDateFormat("EE", Locale.ENGLISH).format(date.time))
        if (currentDay == "Mon"){
            return 0
        }

        else if (currentDay == "Tue"){
        return 1
        }

        else if (currentDay == "Wed"){
            return 2
        }

        else if (currentDay == "Thu"){
        return 3
        }

        else if (currentDay == "Fri"){
        return 4
        }

        else if (currentDay == "Sat"){
        return 5
        }

        else if (currentDay == "Sun"){
            return 6
        }
        else {
            return 0
        }
    }


    fun getDistance(latCurrentLocation:Double,lngCurrentLocation:Double,latRestaurantLocation:Double,lngRestaurantLocation:Double):String{



        val startPoint = Location("locationA")
        startPoint.setLatitude(latCurrentLocation)
        startPoint.setLongitude(lngCurrentLocation)

        val endPoint = Location("locationA")
        endPoint.setLatitude(latRestaurantLocation)
        endPoint.setLongitude(lngRestaurantLocation)

        val distance: Float = startPoint.distanceTo(endPoint)

       // var distanceWithNoDemials = distance.roundToInt().toString() + "m"

        println("The distance is " + distance )

        return  distance.roundToInt().toString() + "m"
    }

    fun getPictureUrl(pictureRef:String):String{

        var imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+pictureRef+"&key="+"AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc"

        println("The image is " + imageUrl)

        return imageUrl
    }


    fun saveDetails(restaurantGoingID:String, like:Boolean){
         var mAuth: FirebaseAuth
         var databaseRefrence : DatabaseReference
        databaseRefrence = FirebaseDatabase.getInstance().getReference("Users")
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val user = User(currentUser?.uid,currentUser?.displayName,currentUser?.email,restaurantGoingID,like)

        currentUser?.uid?.let {
            databaseRefrence.child(it).setValue(user).addOnCompleteListener{
                if(it.isSuccessful){
                    //   User this to store profiles
                    Toast.makeText(getApplication(),"User Created", Toast.LENGTH_SHORT).show()

                } else{
                    Toast.makeText(getApplication(),"Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
        println("Create Profile")
    }


}