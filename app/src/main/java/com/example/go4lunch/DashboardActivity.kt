package com.example.go4lunch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.go4lunch.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class DashboardActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        val IdTxt = findViewById<TextView>(R.id.id_txt)
        val nameTxt = findViewById<TextView>(R.id.name_txt)
        val emailTxt = findViewById<TextView>(R.id.email_txt)


        val signOutBtn = findViewById<Button>(R.id.sign_out_btn)
        val profileImg = findViewById<ImageView>(R.id.profile_image)




        IdTxt.text = currentUser?.uid
        nameTxt.text = currentUser?.displayName
        emailTxt.text = currentUser?.email

       // Glide.with(this).load(currentUser?.photoUrl).into(profile_image)
        val picasso = Picasso.get()
        picasso.load(currentUser?.photoUrl).into(profileImg)

        signOutBtn.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
