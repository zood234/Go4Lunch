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

class AllRestaurantsViewModel(application: Application) : AndroidViewModel(application) {

    var allRest = arrayListOf<AllItems>()
var liveDataRestaurants: MutableLiveData<Boolean> = MutableLiveData()




//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.50979902325245,0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc

    fun makeApiNearbyCall(latlng: String): List<AllItems> {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)

                var response = retroInstance.getAllNearbyRest(latlng,"5000","restaurant","AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")


                makeApiPlaceDetails(response.results[3].place_id)


                for (i in 0..response.results.size-1) {
                    println("The response is " +response.results[i].name)
                    println("Place Id is "+ response.results[i].place_id)

                    var tempObjectOfResponse = AllItems(response.results[i].name,response.results[i].place_id,response.results[i].place_id,response.results[i].geometry.location.lng
                    ,response.results[i].geometry.location.lat,response.results[i].rating,response.results[i].vicinity,response.results[i].types[0],response.results[i].place_id
                    , "NOT PART OF THE API CALL DUE TO ARTIFICIAL LIMIT","1")

                    allRest.add(tempObjectOfResponse)
                    liveDataRestaurants.postValue(true)
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
                println("The response for place id " +response.result.opening_hours.weekday_text)
            }
        } catch (e: IOException) {
            println("response did not work")
            e.printStackTrace()

        }
    }





}