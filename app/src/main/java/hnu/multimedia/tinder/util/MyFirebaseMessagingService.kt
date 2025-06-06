package hnu.multimedia.tinder.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("MyFirebaseMessagingService", "onNewToken: $token")
        val currentUserId = FirebaseRef.currentUserId
        if (currentUserId != "") {
            FirebaseRef.fcmTokens.child(currentUserId).setValue(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            Log.d("MyFirebaseMessagingService", "onMessageReceived: ${it.title}")
            NotificationUtil.createNotification(this, it.title ?: "No Title", it.body ?: "No Body")
        }
    }
}