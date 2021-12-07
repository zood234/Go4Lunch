package com.example.go4lunch.models.nearbysearch.placedetails

data class OpeningHours(
    val open_now: Boolean,
    val periods: List<Period>,
    val weekday_text: List<String>
)