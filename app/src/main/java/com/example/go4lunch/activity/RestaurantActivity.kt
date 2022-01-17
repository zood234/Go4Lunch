package com.example.go4lunch.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.go4lunch.others.AllRestaurantsViewModel
import com.example.go4lunch.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.adapters.AllUsersRVAdapter
import com.example.go4lunch.models.nearbysearch.User

import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurant.*

class RestaurantActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    lateinit var allDataUsers : ArrayList<User>
    lateinit var alluserRV : RecyclerView
    var colorChanger = false
    private lateinit var viewModel: AllRestaurantsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        alluserRV= findViewById<RecyclerView>(R.id.allUsersGoingToTheRestaurnatRV)
        alluserRV.layoutManager = LinearLayoutManager(this)
        alluserRV.setHasFixedSize(true)
        allDataUsers = arrayListOf<User>()
        var phone = ""
        var website = ""
        var name =""
        val intent = intent
        val id = intent.getStringExtra("Name")
        var like = false
        viewModel = ViewModelProvider(this).get(AllRestaurantsViewModel::class.java)
        if (id != null) {
            getUserData(id)
        }
        if (id != null) {

            var placeDetais = viewModel.makeApiPlaceDetails(id)


        }

        viewModel.liveRestDetails.observe(this, androidx.lifecycle.Observer{
            name = it.name
            titleRestaurantTV.text=name
            catAndAddressTV.text =it.type + " - " + it.address
            var picture = viewModel.getPictureUrl(it.pictureUrl)

println("THE PICTURE URL IS "+ picture)
            val picasso = Picasso.get()
            picasso.load(it.pictureUrl).into(RestaurnatIV)
            phone= it.phone
            website = it.website


        })



callBtn.setOnClickListener {
    val dialIntent = Intent(Intent.ACTION_DIAL)
    dialIntent.data = Uri.parse("tel:" + phone)
    startActivity(dialIntent)
}
    websiteBtn.setOnClickListener {
        if (website == ""){
            Toast.makeText(this, "There is no website available", Toast.LENGTH_SHORT).show()
        }

        else {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(website)
            startActivity(i)
        }
    }

        goingBtn.setOnClickListener {
            if (id != null) {
                viewModel.saveDetails(id,like,name)
            }

        }


        likeBtn.setOnClickListener {
            if (colorChanger== false){
                likeBtn.setBackgroundColor(
                    Color.parseColor("#FF5722"))
                colorChanger = true
            }

            else {
                likeBtn.setBackgroundColor(
                    Color.parseColor("#FF6200EE"))
                like = false

            }



        }
    }



    private fun getUserData(id:String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (user.restrauntID== id) {
                                allDataUsers.add(user)
                            }
                        }

                    }
                    alluserRV.adapter = AllUsersRVAdapter(allDataUsers)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error")
            }


        }

        )
    }







}
