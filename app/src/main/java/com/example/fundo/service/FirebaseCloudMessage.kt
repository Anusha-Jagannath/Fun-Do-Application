package com.example.fundo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.fundo.MyWorker
import com.example.fundo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseCloudMessage : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("DIY", "Refreshed token $token")
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("TAG", "From: " + remoteMessage.from)
        if (remoteMessage.data.isNotEmpty()) {
            Toast.makeText(applicationContext, "${remoteMessage.data}", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "Message data payload: " + remoteMessage.data)
        }
        if (remoteMessage.notification != null) {
            Log.d("TAG", "Message Notification Body: " + remoteMessage.notification!!.body)
        }

        val title = remoteMessage.data
        val content = remoteMessage.notification
        

    }
}