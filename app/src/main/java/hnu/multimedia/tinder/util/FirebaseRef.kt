package hnu.multimedia.tinder.util

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object {
        private val database = Firebase.database
        val users = database.getReference("users")
        var currentUserId = Firebase.auth.currentUser?.uid ?: ""
        var likes = database.getReference("likes")

        fun initCUid() {
            currentUserId = Firebase.auth.currentUser?.uid ?: ""
            MyData.init()
        }
    }
}