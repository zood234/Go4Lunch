package com.example.go4lunch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference

class AllUsersActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var user:User
    lateinit var allDataUsers : ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        allDataUsers = arrayListOf<User>()
        getUserData()

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
    }




}