package com.example.go4lunch.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.R
import com.example.go4lunch.models.nearbysearch.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import java.io.File

class AllUsersRVAdapter(private val mList: List<User>) : RecyclerView.Adapter<AllUsersRVAdapter.ViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_all_users_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()

        val ItemsViewModel = mList[position]
       holder.textViewActor.text = ItemsViewModel.displayName
       holder.textViewCharacter.text = ""
        holder.textViewHouse.text = ""

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
        val imageView: ImageView = itemView.findViewById(R.id.userIV)
        val textViewActor: TextView = itemView.findViewById(R.id.actorTV)
        val textViewCharacter: TextView = itemView.findViewById(R.id.characterTV)
        val textViewHouse: TextView = itemView.findViewById(R.id.houseTV)

    }
}