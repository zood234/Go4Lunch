package com.example.go4lunch.others

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.example.go4lunch.*
import com.example.go4lunch.R
import com.example.go4lunch.interfaces.NearByRestApi
import com.example.harrypottercaracters.RetroInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

const val channelID = "notifaction_channel"
const val channelName = "com.example.go4lunch"
class MyFirebaseMessagingService: FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
    if (remoteMessage.getNotification() != null){
        println("Notifaction received")
        getCurrentUserReastaurantID()
    }


    }
fun generateNotification(title:String, description: String ){
    val intent = Intent(this,MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

    val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

    var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
        channelID)
        .setSmallIcon(R.drawable.food_icon)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .setContentIntent(pendingIntent)

    builder = builder.setContent(getRemoteView(title, description))
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val notifactionChannel = NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notifactionChannel)
    }
    notificationManager.notify(0,builder.build())

}

    fun getRemoteView(title:String, description: String ):RemoteViews{
        val remoteView = RemoteViews("com.example.go4lunch",R.layout.notification_layout)
        remoteView.setTextViewText(R.id.notificationTitleTV,title)
        remoteView.setTextViewText(R.id.notificationDescTV,description)
        remoteView.setImageViewResource(R.id.notificationLogoIV,R.drawable.food_icon)
        return remoteView
    }





    fun makeApiPlaceDetails(placeID: String, description: String) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val retroInstance = RetroInstance.getInstance().create(NearByRestApi::class.java)
                var response = retroInstance.getPlaceDetails(placeID,"AIzaSyBE5fuDypxo9mLKBderC-7GTMmnF57ghbc")
                generateNotification(response.result.name,description)

            }
        } catch (e: IOException) {
            println("response did not work")
            e.printStackTrace()

        }
    }

    fun getCurrentUser():String{
        var auth: FirebaseAuth
        var  uid :String
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        println("THE UID IS " + uid )
        return uid

    }

    private fun getAllUsers(){
        var currentUserID = getCurrentUser()
        var allDataUsers = ArrayList<User>()
        var databaseReference: DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                      allDataUsers.add(user!!)
                    }
                    checkIfAnyUserHasSameReastrauntID(currentUserID, allDataUsers)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                println("Error")
            }
        }

        )
    }





fun checkIfAnyUserHasSameReastrauntID(currentUserID: String, allDataUsers: ArrayList<User>){
    var amountOfPeopleGoing = 0
    var restaurantID = ""

    for(i in 0..allDataUsers.size-1){

        if(currentUserID == allDataUsers[i].userId)
        {
            restaurantID = allDataUsers[i].restrauntID.toString()

        }
    }

        for(i in 0..allDataUsers.size-1){
            if (restaurantID == allDataUsers[i].restrauntID && restaurantID != ""){
                println(restaurantID + " and then compare it to" + allDataUsers[i].restrauntID )
                amountOfPeopleGoing++
            }
        }

        var  description = "Dont forget to go to the reastraunt. There will be "+ amountOfPeopleGoing.toString() + " going."


    if (restaurantID != "") {
        makeApiPlaceDetails(restaurantID, description)
    }
}




    fun getCurrentUserReastaurantID(){

        getAllUsers()
}













}