package com.example.harrypottercaracters

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RetroInstance {
    companion object APIUtil {
        private val Base_URL = "https://maps.googleapis.com/maps/api/place/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}