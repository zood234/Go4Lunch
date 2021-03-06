package com.example.go4lunch.adapters

import android.R.attr
import android.content.Context
import android.content.Intent
import android.graphics.Picture
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go4lunch.R
import com.example.go4lunch.activity.RestaurantActivity
import com.example.go4lunch.models.nearbysearch.AllItems
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_all_resaurants_design.view.*
import android.R.attr.data
import android.R.attr.data







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

    fun deleteItems() {

    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = allRest.get(position)

        if (item != null) {
            holder.titleTv.text = item.titleList
            holder.catTv.text = item.genreList
            holder.openingTimeTv.text = item.openingHours
            holder.distanceTv.text = item.distance
            holder.reviewTv.text = item.ratingList.toString()+"/5"

            val picasso = Picasso.get()
            picasso.load(item.iamgerefList).into(holder.restIV)

//            if (item.amountOfPeopleGoing == "0" ){
//                holder.amountOfPeopleTv.text = "No one is going here be the first"
//            }
//
//            else{
//                holder.amountOfPeopleTv.text = "The amount of people going here are " + item.amountOfPeopleGoing.toString()
//            }

            holder.cardViewItem.setOnClickListener {
            val intent = Intent(mContext, RestaurantActivity::class.java)
            intent.putExtra("Name", item.idList)
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