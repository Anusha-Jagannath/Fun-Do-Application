package com.example.fundo.service

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseCloudMessage : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("New token", "Refreshed token $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("Token", "From: " + remoteMessage.from)
        if (remoteMessage.data.isNotEmpty()) {
            Toast.makeText(applicationContext, "${remoteMessage.data}", Toast.LENGTH_SHORT).show()
            Log.d("Token", "Message data payload: " + remoteMessage.data)
        }
        if (remoteMessage.notification != null) {
            Log.d("Token", "Message Notification Body: " + remoteMessage.notification!!.body)
        }
        Log.d("message", "Message received")
    }
}