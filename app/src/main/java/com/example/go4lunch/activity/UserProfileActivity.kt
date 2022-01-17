package com.example.go4lunch.activity

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.go4lunch.R
import com.example.go4lunch.adapters.AllUsersActivity
import com.example.go4lunch.models.nearbysearch.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.nav_header.*
import java.io.File

class UserProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var user: User
    private lateinit var  uid :String
    var bitmap = BitmapFactory.decodeFile(R.drawable.collapse.toString())

lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        userAllUsersBtn.setOnClickListener {
            val intent = Intent(this, AllUsersActivity::class.java)
            startActivity(intent)

        }


        userMapBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }

        userRestListBtn.setOnClickListener {
            val intent = Intent(this, ListofRestaurantsActivity::class.java)
            startActivity(intent)

        }




navView.setNavigationItemSelectedListener {

    when(it.itemId){
        R.id.mYourLunch -> Toast.makeText(applicationContext,"item 1",Toast.LENGTH_SHORT).show()
        R.id.mSettings -> Toast.makeText(applicationContext,"item 2",Toast.LENGTH_SHORT).show()
        R.id.mLogout -> signOut()

    }
    true
}




        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){
            getUserData()
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        nameSideBarTV.text = user.displayName
        emailSideBarTV2.text = user.email
        profilePicIV.setImageBitmap(bitmap)
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    //inflates the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
    //val searchItem = menu?.findItem(R.id.search_box)
 val searchItem2 = menu?.findItem(R.id.auto_search_box)


        val typesOfFood: Array<out String> = resources.getStringArray(R.array.food_type)

        if (searchItem2 != null) {
            val searchView2 = searchItem2.actionView as AutoCompleteTextView

            ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,typesOfFood).also {
                adapter -> searchView2.setAdapter(adapter)
                searchView2.setOnItemClickListener { parent, view, position, id -> println("It Worked " + searchView2.text) }/// Put the function for retro fit here
        }




        }

        return true

        }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                val nameTxt = findViewById<TextView>(R.id.tvFullname)
                val emailTxt = findViewById<TextView>(R.id.tvEmail)
                nameTxt.text = user.displayName
                emailTxt.text = user.email
                getUserProfilePicture()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getUserProfilePicture() {
        val profileIv = findViewById<ImageView>(R.id.circleImageView)
      storageReference = FirebaseStorage.getInstance().reference.child("users/" +uid+ ".jpg") //  "users/" +uid)

        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
           bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            profileIv.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to retrieve image ", Toast.LENGTH_SHORT).show()
        }
    }

    fun signOut() {
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance()

        mAuth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}