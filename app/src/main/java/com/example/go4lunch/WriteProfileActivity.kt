package com.example.go4lunch

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_write_profile.*
import kotlinx.coroutines.*

class WriteProfileActivity : AppCompatActivity() {
    var doesTheUserExist: MutableLiveData<Boolean> = MutableLiveData()

     lateinit var mAuth: FirebaseAuth
      lateinit var databaseRefrence :DatabaseReference
     lateinit var storageRefrence: StorageReference
     private lateinit var  imageUri : Uri
    private val pickImage = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_profile)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        databaseRefrence = FirebaseDatabase.getInstance().getReference("Users")

        val user = User(currentUser?.uid,currentUser?.displayName,currentUser?.email,"",false)

      //  addUsersMockData()
        //Working but need to make so the user uploads the profile



        val idTxt = findViewById<TextView>(R.id.id_txt)
        val nameTxt = findViewById<TextView>(R.id.name_txt)
        val emailTxt = findViewById<TextView>(R.id.email_txt)
        val profileImg = findViewById<ImageView>(R.id.profile_image)
        val createProfieBtn = findViewById<Button>(R.id.createProfileBtn)
        val uploadImageBtn = findViewById<Button>(R.id.uploadImageBtn)
        idTxt.text = currentUser?.uid
        nameTxt.text = currentUser?.displayName
        emailTxt.text = currentUser?.email
        createProfieBtn.visibility = View.GONE
        val picasso = Picasso.get()
        picasso.load(currentUser?.photoUrl).into(profileImg)
        println("IMAGE IS " + profileImg)


            //checkIfUserExists(currentUser?.uid)
        getUserData(currentUser?.uid)


        doesTheUserExist.observe(this, androidx.lifecycle.Observer{

            if (it == true) {
                val i = Intent(this, MapsActivity::class.java)
                startActivity(i)            }
        })





        createProfieBtn.setOnClickListener {
            createProfile()
    }

        uploadImageBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }
    }

    fun createProfile(){
        val currentUser = mAuth.currentUser
        val user = User(currentUser?.uid,currentUser?.displayName,currentUser?.email,"",false)

        currentUser?.uid?.let {
            databaseRefrence.child(it).setValue(user).addOnCompleteListener{
                if(it.isSuccessful){
                    //   User this to store profiles
                    uploadProfilePic(currentUser?.uid, imageUri)
                    Toast.makeText(this,"User Created", Toast.LENGTH_SHORT).show()

                } else{
                    Toast.makeText(this,"Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
        println("Create Profile")
    }
    private fun uploadProfilePic( uid: String?, uri: Uri?) {
        val createProfieBtn = findViewById<Button>(R.id.createProfileBtn)

       // imageUri = Uri.parse("android.resource://$packageName/${R.drawable.profile}")

        if (uri != null) {
            imageUri = uri
        }

        storageRefrence = FirebaseStorage.getInstance().getReference("users/" + uid+ ".jpg")
        storageRefrence.putFile(imageUri).addOnSuccessListener {
            createProfieBtn.visibility = View.VISIBLE

        }.addOnFailureListener{
        Toast.makeText(this,"Failed to update image", Toast.LENGTH_SHORT).show()
            }
    }

//     fun checkIfUserExists(currentUserID: String?){
//        if (getUserData(currentUserID)==true){
//            println("This has been activated")
//            val i = Intent(this, UserProfileActivity::class.java)
//            startActivity(i)
//        }
//         if (getUserData(currentUserID)==false){
//            println("Create user")
//        }
//    }

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

    private fun getUserData(id:String?){

        var databaseReference: DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            if (user.userId== id){
                                println("THE USER EXISTS")
                                doesTheUserExist.postValue(true)
                            }

                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error")
            }


        })



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data!!
            profile_image.setImageURI(imageUri)

                uploadProfilePic("d34s", imageUri)



        }
    }


}
