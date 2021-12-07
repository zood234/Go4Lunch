package com.example.go4lunch.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.go4lunch.R
import kotlinx.android.synthetic.main.activity_restaurant.*

class RestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        val intent = intent
        val name = intent.getStringExtra("Name")
        textView2.text = name
    }
}