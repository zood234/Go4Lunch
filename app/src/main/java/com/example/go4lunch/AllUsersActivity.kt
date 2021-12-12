package com.example.go4lunch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_all_users.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.nav_header.*

class AllUsersActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var allDataUsers : ArrayList<User>
    lateinit var alluserRV : RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)
        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        alluserRV= findViewById<RecyclerView>(R.id.allUsersRV)
        alluserRV.layoutManager = LinearLayoutManager(this)
        alluserRV.setHasFixedSize(true)
        allDataUsers = arrayListOf<User>()
        toggle = ActionBarDrawerToggle(this,drawerLayoutAllUsers,R.string.open, R.string.close)
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





        allUsersMapBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }

        allUsersListBtn.setOnClickListener {
            val intent = Intent(this, ListofRestaurantsActivity::class.java)
            startActivity(intent)

        }



    }

    //inflates the menu
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
                        allDataUsers.add(user!!)
                     //   println("The third user is " + allDataUsers)

                    }
                    alluserRV.adapter = AllUsersRVAdapter(allDataUsers)

                    addtoRecyclerview()
                }
            }

            override fun onCancelled(error: DatabaseError) {
               println("Error")
            }


        }








        )








    }

    private fun addtoRecyclerview() {

        println(allDataUsers[2])
//        val recyclerview = findViewById<RecyclerView>(R.id.allUsersRV)
//        val adapter = RVAdapter(allDataUsers)
//        recyclerview.adapter = adapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        textView3.text = "blah balh"
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}