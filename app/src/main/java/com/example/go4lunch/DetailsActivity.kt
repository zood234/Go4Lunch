package com.example.go4lunch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val facebookId = intent.getStringExtra("facebook_id")
        val facebookFirstName = intent.getStringExtra("facebook_first_name")
        val facebookMiddleName = intent.getStringExtra("facebook_middle_name")
        val facebookLastName = intent.getStringExtra("facebook_last_name")
        val facebookName = intent.getStringExtra("facebook_name")
        val facebookPicture = intent.getStringExtra("facebook_picture")
        val facebookEmail = intent.getStringExtra("facebook_email")
        val facebookAccessToken = intent.getStringExtra("facebook_access_token")


        val txtView = findViewById<TextView>(R.id.textView2)

        println("DETAILS ACTIVITY =" + facebookFirstName)
        txtView.text = "DETAILS ACTIVITY =" + facebookFirstName
//        facebook_id_textview.text = facebookId
//        facebook_first_name_textview.text = facebookFirstName
//        facebook_middle_name_textview.text = facebookMiddleName
//        facebook_last_name_textview.text = facebookLastName
//        facebook_name_textview.text = facebookName
//        facebook_profile_picture_url_textview.text = facebookPicture
//        facebook_email_textview.text = facebookEmail
//        facebook_access_token_textview.text = facebookAccessToken

    }
}
