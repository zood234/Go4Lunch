package com.example.go4lunch.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.AllUsersRVAdapter
import com.example.go4lunch.R
import com.example.go4lunch.User
import com.example.go4lunch.activity.RestaurantActivity
import com.example.go4lunch.models.nearbysearch.AllItems
import com.example.go4lunch.models.nearbysearch.Restaurants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.card_view_all_resaurants_design.view.*
import kotlinx.android.synthetic.main.card_view_all_users_design.view.*

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


        val item = allRest.get(position)

        if (item != null) {
            holder.titleTv.text = item.titleList
            holder.catTv.text = item.genreList
            holder.openingTimeTv.text = item.openingHours
            holder.distanceTv.text = item.latList.toString() + " " + item.lngList.toString()
            holder.amountOfPeopleTv.text = item.amountOfPeopleGoing
            holder.reviewTv.text = item.ratingList.toString()


            holder.cardViewItem.setOnClickListener {
            val intent = Intent(mContext, RestaurantActivity::class.java)
            intent.putExtra("Name", item.titleList)
            mContext.startActivity(intent)

            }
        }
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to

        val titleTv = view.titleTV
        val restIV = view.restaurantIV
        val catTv = view.catTV
        val openingTimeTv = view.openingTimeTV
        val distanceTv = view.distanceTV
        val amountOfPeopleTv = view.amountOfPeopleTV
        val reviewTv = view.reviewTV
        val cardViewItem = view.cardViewItem


    }


}