package com.example.go4lunch.models.nearbysearch.nearbySearch

data class nearByRest(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)