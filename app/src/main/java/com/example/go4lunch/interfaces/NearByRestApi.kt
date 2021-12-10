package com.example.go4lunch.interfaces


import com.example.go4lunch.models.nearbysearch.nearbySearch.nearByRest
import com.example.go4lunch.models.nearbysearch.placedetails.placeDetails
import retrofit2.http.GET
import retrofit2.http.Query


//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.50979902325245,0.12624660554017764&radius=5000&type=restaurant&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc
interface NearByRestApi {


    @GET("nearbysearch/json?")
    suspend fun getAllNearbyRest(
        @Query("location") loc: String?,
        @Query("radius") radius: String?,
        @Query("type") type: String?, @Query("key") key: String?
    ): nearByRest


    //https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJq5XIW8oEdkgRqmg5RAC_ih8&key=AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc
    @GET("details/json?")
    suspend fun getPlaceDetails(
        @Query("place_id") loc: String?, @Query("key") key: String?
    ): placeDetails


}