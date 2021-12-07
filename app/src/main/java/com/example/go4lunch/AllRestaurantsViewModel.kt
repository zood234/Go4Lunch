package com.example.go4lunch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.go4lunch.interfaces.NearByRestApi
import com.example.go4lunch.models.nearbysearch.AllItems
import com.example.go4lunch.models.nearbysearch.Restaurants
import com.example.harrypottercaracters.RetroInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class AllRestaurantsViewModel(application: Application) : AndroidViewModel(application) {
    var test = "view model works"

    private var allLiveData: ArrayList<AllItems> = ArrayList()
//
//    lateinit var allRestaurants : ArrayList<Restaurants>
//    var allRestaurants = ArrayList<String?>()

var liveDataRestaurants: MutableLiveData<AllItems> = MutableLiveData()

    var titleList = ArrayList<String?>()
    var distanceList = ArrayList<String?>()
    var iamgerefList = ArrayList<String?>()
    var lngList = ArrayList<Double?>()
    var latList = ArrayList<Double?>()
    var ratingList = ArrayList<Double?>()
    var addressList = ArrayList<String?>()
    var genreList = ArrayList<String?>()
    var idList = ArrayList<String?>()
    var openingHours = ArrayList<String?>()
    var amountOfPeopleGoing = ArrayList<String?>()



//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.50979902325245,0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc

    fun makeApiNearbyCall(latlng: String): List<AllItems> {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)

                var response = retroInstance.getAllNearbyRest(latlng,"5000","restaurant","AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")

              //  val response = retroInstance.getAllNearbyRest2()

                makeApiPlaceDetails(response.results[3].place_id)


                for (i in 0..response.results.size-1) {
                    println("The response is " +response.results[i].name)
                    println("Place Id is "+ response.results[i].place_id)
                     titleList.add(response.results[i].name)
                    idList.add(response.results[i].place_id)
                    iamgerefList.add(response.results[i].photos[0].photo_reference)
                     lngList.add(response.results[i].geometry.location.lng)
                     latList.add(response.results[i].geometry.location.lat)
                     ratingList.add(response.results[i].rating)
                     addressList.add(response.results[i].vicinity)
                     genreList.add(response.results[i].types[0])
                     openingHours.add("NOT PART OF THE API CALL DUE TO ARTIFICIAL LIMIT")
                    amountOfPeopleGoing.add("1")

                    var tempObjectOfResponse = AllItems(response.results[i].name,response.results[i].place_id,response.results[i].place_id,response.results[i].geometry.location.lng
                    ,response.results[i].geometry.location.lat,response.results[i].rating,response.results[i].vicinity,response.results[i].types[0],response.results[i].place_id
                    , "NOT PART OF THE API CALL DUE TO ARTIFICIAL LIMIT","1")

                    allRest.add(tempObjectOfResponse)
//                allRest[i].titleList = response.results[i].name
//                    allRest[i].iamgerefList = response.results[i].photos[0].photo_reference
//                   allRest[i].lngList = response.results[i].geometry.location.lng
//                    allRest[i].latList = response.results[i].geometry.location.lat
//                    allRest[i].ratingList = response.results[i].rating
//                    allRest[i].addressList = response.results[i].name
//                    allRest[i].addressList = response.results[i].vicinity
//                    allRest[i].openingHours = "NOT PART OF THE API CALL DUE TO ARTIFICIAL LIMIT"
//                    allRest[i].amountOfPeopleGoing = "1"
                    var a = Restaurants()
                   liveDataRestaurants.postValue(tempObjectOfResponse)
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

                //  val response = retroInstance.getAllNearbyRest2()
                println("The response for place id " +response.result.opening_hours.weekday_text)
            //    println("Place Id is "+ response.results[3].place_id)
            }
        } catch (e: IOException) {
            println("response did not work")
            e.printStackTrace()

        }
    }


    fun getAllLiveObserver(): MutableLiveData<AllItems> {
        return liveDataRestaurants
    }






}