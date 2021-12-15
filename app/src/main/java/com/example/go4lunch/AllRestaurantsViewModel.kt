package com.example.go4lunch

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.go4lunch.interfaces.NearByRestApi
import com.example.go4lunch.models.nearbysearch.AllItems
import com.example.go4lunch.models.nearbysearch.Restaurants
import com.example.harrypottercaracters.RetroInstance
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AllRestaurantsViewModel(application: Application) : AndroidViewModel(application) {

    var allRest = arrayListOf<AllItems>()
var liveDataRestaurants: MutableLiveData<Boolean> = MutableLiveData()




//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.50979902325245,0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc

    fun makeApiNearbyCall(latlng: String): List<AllItems> {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)

                var response = retroInstance.getAllNearbyRest(latlng,"5000","restaurant","AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")

               // makeApiPlaceDetails(response.results[3].place_id)
                for (i in 0..response.results.size-1) {

                    println("The response is " +response.results[i].name)
                    println("Place Id is "+ response.results[i].place_id)

                   // makeApiPlaceDetails(response.results[3].place_id)

                    makeApiPlaceDetails(response.results[i].place_id)

                    try {
                        viewModelScope.launch(Dispatchers.IO) {
                            val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)
                            var responsePlace = retroInstance.getPlaceDetails(response.results[i].place_id,"AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")
                        //  println("The response for place id " +responsePlace.result.name)

                          //  println("The lat lng to check is "+ response.results[i].place_id)
                              //      println(responsePlace)
                            var time = ""
                            if (responsePlace.result.opening_hours !=null ){
                                 time = responsePlace.result.opening_hours.weekday_text[getCurrentDay()]
                               println("The response for place time " + responsePlace.result.opening_hours.weekday_text[getCurrentDay()])
                            }
                            var tempObjectOfResponse = AllItems(response.results[i].name,response.results[i].place_id,response.results[i].place_id,response.results[i].geometry.location.lng
                    ,response.results[i].geometry.location.lat,response.results[i].rating,response.results[i].vicinity,response.results[i].types[0],response.results[i].place_id
                    , time,"1")

                    allRest.add(tempObjectOfResponse)
                            liveDataRestaurants.postValue(true)

                        }
                    } catch (e: IOException) {
                        println("response did not work")
                        e.printStackTrace()

                    }

//
//                    var tempObjectOfResponse = AllItems(response.results[i].name,response.results[i].place_id,response.results[i].place_id,response.results[i].geometry.location.lng
//                    ,response.results[i].geometry.location.lat,response.results[i].rating,response.results[i].vicinity,response.results[i].types[0],response.results[i].place_id
//                    , "NOT PART OF THE API CALL DUE TO ARTIFICIAL LIMIT","1")
//
//                    allRest.add(tempObjectOfResponse)






                }
               // liveDataRestaurants.postValue(all)

            }
        } catch (e: IOException) {
            println("response did not work")
            e.printStackTrace()

        }
        return allRest
    }


    fun makeApiPlaceDetails(placeID: String) {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)
                var response = retroInstance.getPlaceDetails(placeID,"AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")
//              if (response.result.opening_hours.weekday_text.isNotEmpty() ) {
//                    println("The response for place time " + response.result.opening_hours.weekday_text[0])
//                }
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





}