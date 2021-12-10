package com.example.go4lunch

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.File

class UserProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var user:User
    private lateinit var  uid :String
lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
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
        R.id.mLogout -> Toast.makeText(applicationContext,"item 3",Toast.LENGTH_SHORT).show()

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
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    //inflates the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)

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
//tried gs://go4lunch-3b949.appspot.com/users/utpinVrTiYdwz6KWvKlm8o36ynq2
        //gs://go4lunch-3b949.appspot.com/users/2GoJwIWoU4YVMFsRBE0MiHRAXk43
        // did not work gs://go4lunch-3b949.appspot.com/users/1.jpg
      storageReference = FirebaseStorage.getInstance().reference.child("users/" +uid) //  "users/" +uid)

        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
          val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            profileIv.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to retrieve image ", Toast.LENGTH_SHORT).show()
        }
    }


}