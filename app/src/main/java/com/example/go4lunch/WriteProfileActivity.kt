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

class WriteProfileActivity : AppCompatActivity() {

     lateinit var mAuth: FirebaseAuth
      lateinit var databaseRefrence :DatabaseReference
     lateinit var storageRefrence: StorageReference
     private lateinit var  imageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_profile)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        databaseRefrence = FirebaseDatabase.getInstance().getReference("Users")

        val user = User(currentUser?.uid,currentUser?.displayName,currentUser?.email,"",false)

        addUsersMockData()
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

        val userProfilebtn = findViewById<Button>(R.id.userProfileBtn)
        idTxt.text = currentUser?.uid
        nameTxt.text = currentUser?.displayName
        emailTxt.text = currentUser?.email

        val picasso = Picasso.get()
        picasso.load(currentUser?.photoUrl).into(profileImg)
        println("IMAGE IS " + profileImg)

        signOutBtn.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        userProfilebtn.setOnClickListener {
            val i = Intent(this, UserProfileActivity::class.java)
            startActivity(i)
        }
    }


    private fun uploadProfilePic( uid: String?, uri: Uri?) {

        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.profile}")
        storageRefrence = FirebaseStorage.getInstance().getReference("users/" + uid)
        storageRefrence.putFile(imageUri).addOnSuccessListener {
        }.addOnFailureListener{
        Toast.makeText(this,"Failed to update image", Toast.LENGTH_SHORT).show()
            }


    }

    fun addUsersMockData(){
        val mockDataUsers = arrayOfNulls<User>(10)
        val user1 = User("1","Anakin Skywalker","1@gmail.com","",false)
        val user2 = User("2","Luke Skywalker","2@gmail.com","",false)
        val user3 = User("3","Leia Organa","3@gmail.com","",false)
        val user4 = User("4","Han Solo","4@gmail.com","",false)
        val user5 = User("5","Ben Solo","5@gmail.com","",false)
        val user6 = User("6","Obi-Wan Kenobi","6@gmail.com","",false)
        val user7 = User("7","Mace Windu","7@gmail.com","",false)
        val user8 = User("8","Sheev Palpatine","8@gmail.com","",false)
        val user9 = User("9","Lando Calrissian","9@gmail.com","",false)
        val user10 = User("10","Jyn Erso","10@gmail.com","",false)

        mockDataUsers[0]= user1
        mockDataUsers[1]= user2
        mockDataUsers[2]= user3
        mockDataUsers[3]= user4
        mockDataUsers[4]= user5
        mockDataUsers[5]= user6
        mockDataUsers[6]= user7
        mockDataUsers[7]= user8
        mockDataUsers[8]= user9
        mockDataUsers[9]= user10


   //     databaseRefrence.setValue(user1.userId)


        user1.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user1)
                }
            }


        user2.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user2)
            }
        }


        user3.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user3)
            }
        }
        user4.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user4)
            }
        }
        user5.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user5)
            }
        }
        user6.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user6)
            }
        }
        user7.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user7)
            }
        }
        user9.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user8)
            }
        }


        user9.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user9)
            }
        }
        user10.userId.let {
            if (it != null) {
                databaseRefrence.child(it).setValue(user10)
            }
        }

    }

}
