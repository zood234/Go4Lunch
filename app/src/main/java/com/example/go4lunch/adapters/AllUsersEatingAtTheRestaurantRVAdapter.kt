package com.example.go4lunch.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.R
import com.example.go4lunch.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import java.io.File

class AllUsersEatingAtTheRestaurantRVAdapter(private val mList: List<User>) : RecyclerView.Adapter<AllUsersEatingAtTheRestaurantRVAdapter.ViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_restaurant, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()

        val ItemsViewModel = mList[position]
       holder.userTV.text = ItemsViewModel.displayName

        storageReference = FirebaseStorage.getInstance().reference.child("users/" +ItemsViewModel.userId+".jpg")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imageView.setImageBitmap(bitmap)
        }.addOnFailureListener{


        }



    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.userEatingIV)
        val userTV: TextView = itemView.findViewById(R.id.userEatingTV)

    }
}