package com.example.go4lunch

import android.app.Dialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UserProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var user:User
    private lateinit var  uid :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){
            getUserData()
        }
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                val nameTxt = findViewById<TextView>(R.id.tvFullname)
                val emailTxt = findViewById<TextView>(R.id.tvEmail)
                nameTxt.text = user.displayName
                emailTxt.text = user.email
                getUserProfile()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getUserProfile() {
        val profileIv = findViewById<ImageView>(R.id.circleImageView)

      storageReference = FirebaseStorage.getInstance().reference.child("users/" +uid)
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
          val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            profileIv.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to retrieve image ", Toast.LENGTH_SHORT).show()
        }
    }


}