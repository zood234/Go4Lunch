package com.example.go4lunch

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.sql.Types.NULL

class DashboardActivity : AppCompatActivity() {

     lateinit var mAuth: FirebaseAuth
      lateinit var databaseRefrence :DatabaseReference
     lateinit var storageRefrence: StorageReference
     private lateinit var  imageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        databaseRefrence = FirebaseDatabase.getInstance().getReference("Users")

        val user = User(currentUser?.uid,currentUser?.displayName,currentUser?.email,"",false)


        currentUser?.uid?.let {
            databaseRefrence.child(it).setValue(user).addOnCompleteListener{
                if(it.isSuccessful){

                 //   User this to store profiles
                    uploadProfilePic(currentUser?.uid, currentUser?.photoUrl)


                } else{
                    Toast.makeText(this,"Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }




        val idTxt = findViewById<TextView>(R.id.id_txt)
        val nameTxt = findViewById<TextView>(R.id.name_txt)
        val emailTxt = findViewById<TextView>(R.id.email_txt)


        val signOutBtn = findViewById<Button>(R.id.sign_out_btn)
        val profileImg = findViewById<ImageView>(R.id.profile_image)




        idTxt.text = currentUser?.uid
        nameTxt.text = currentUser?.displayName
        emailTxt.text = currentUser?.email

       // Glide.with(this).load(currentUser?.photoUrl).into(profile_image)
        val picasso = Picasso.get()
        picasso.load(currentUser?.photoUrl).into(profileImg)
        println("IMAGE IS " + profileImg)

        signOutBtn.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun uploadProfilePic( uid: String?, uri: Uri?) {

        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.profile}")
        storageRefrence = FirebaseStorage.getInstance().getReference("users/" + uid)
        storageRefrence.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this, "It Worked", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
        Toast.makeText(this,"Failed to update image", Toast.LENGTH_SHORT).show()
            }


    }

}
