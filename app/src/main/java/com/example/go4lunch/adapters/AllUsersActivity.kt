package com.example.go4lunch.adapters

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.R
import com.example.go4lunch.activity.ListofRestaurantsActivity
import com.example.go4lunch.activity.MapsActivity
import com.example.go4lunch.activity.SignInActivity
import com.example.go4lunch.activity.UserProfileActivity
import com.example.go4lunch.models.nearbysearch.User
import com.example.go4lunch.others.AllRestaurantsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_all_users.*
import kotlinx.android.synthetic.main.nav_header.*

class AllUsersActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var allDataUsers : ArrayList<User>
    lateinit var alluserRV : RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: AllRestaurantsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)
        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        alluserRV= findViewById<RecyclerView>(R.id.allUsersRV)
        alluserRV.layoutManager = LinearLayoutManager(this)
        alluserRV.setHasFixedSize(true)
        allDataUsers = arrayListOf<User>()
        viewModel = ViewModelProvider(this).get(AllRestaurantsViewModel::class.java)

        toggle = ActionBarDrawerToggle(this,drawerLayoutAllUsers, R.string.open, R.string.close)
//        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navViewAllUsers.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mYourLunch -> startUserProfile()
                R.id.mSettings -> Toast.makeText(applicationContext,"item 2",Toast.LENGTH_SHORT).show()
                R.id.mLogout -> signOut()

            }
            true
        }
        getUserData()
        viewModel.getUserData()





        allUsersMapBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }

        allUsersListBtn.setOnClickListener {
            val intent = Intent(this, ListofRestaurantsActivity::class.java)
            startActivity(intent)

        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)

        return true
    }
    fun startUserProfile(){
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }


    fun signOut() {
        mAuth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()

    }
    private fun getUserData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (user.restrauntID == ""){
                                user.displayName = user.displayName + " has not decided yet."
                            }
                            else{
                                user.displayName = user.displayName + " is going to "+ user.restrauntName

                            }
                        }
                        allDataUsers.add(user!!)
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



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        nameSideBarTV.text = viewModel.userName
        emailSideBarTV2.text = viewModel.email
        profilePicIV.setImageBitmap(viewModel.bitmap)

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}