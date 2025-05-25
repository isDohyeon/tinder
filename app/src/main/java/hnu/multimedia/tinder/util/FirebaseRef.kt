package hnu.multimedia.tinder.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object {
        private val database = Firebase.database
        val users = database.getReference("users")
    }
}