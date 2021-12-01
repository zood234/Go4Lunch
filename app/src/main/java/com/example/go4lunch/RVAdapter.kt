package com.example.go4lunch

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.squareup.picasso.Picasso
import java.io.File

class RVAdapter(private val mList: List<User>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()

        val ItemsViewModel = mList[position]
       holder.textViewActor.text = ItemsViewModel.displayName
       holder.textViewCharacter.text = ItemsViewModel.email
        holder.textViewHouse.text = ItemsViewModel.restrauntID

        storageReference = FirebaseStorage.getInstance().reference.child("users/" +ItemsViewModel.userId+".jpg")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imageView.setImageBitmap(bitmap)
        }.addOnFailureListener{
        }

//        if (ItemsViewModel.imageUrl.isNullOrEmpty()) {
//            holder.imageView.setImageResource(R.drawable.ic_launcher_background)
//        } else {
//            picasso.load(ItemsViewModel.imageUrl).into(holder.imageView)
//        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textViewActor: TextView = itemView.findViewById(R.id.actorTV)
        val textViewCharacter: TextView = itemView.findViewById(R.id.characterTV)
        val textViewHouse: TextView = itemView.findViewById(R.id.houseTV)

    }
}