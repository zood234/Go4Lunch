package com.example.go4lunch.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.AllUsersRVAdapter
import com.example.go4lunch.R
import com.example.go4lunch.User
import com.example.go4lunch.models.nearbysearch.AllItems
import com.example.go4lunch.models.nearbysearch.Restaurants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.card_view_all_resaurants_design.view.*

import java.io.File

class AllRestaurantsRVAdapter( val context: Context, var allRest: List<AllItems>) :
    RecyclerView.Adapter<AllRestaurantsRVAdapter.ViewHolder>() {
    var mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(

            LayoutInflater.from(context).inflate(
                R.layout.card_view_all_resaurants_design,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int {

        return allRest.size
    }

//    fun deleteItems() {
//        title.removeAll(title)
//        publishedDate.removeAll(publishedDate)
//        cat.removeAll(cat)
//        url.removeAll(url)
//        picture.removeAll(picture)
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //val item = title.get(position)

        val item = allRest.get(position)


        if (item != null) {
            holder.tvItem.text = item.titleList
        }


    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to

        val tvItem = view.titleTV
//        val tvDate = view.tv_date
//        val tv_cat = view.tv_cat
//        val iv_picture = view.iv_thumbnail
//        val cardViewItem = view.card_view_item

    }


}