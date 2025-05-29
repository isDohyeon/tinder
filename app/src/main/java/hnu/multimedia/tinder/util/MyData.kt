package hnu.multimedia.tinder.util

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hnu.multimedia.tinder.auth.UserModel

class MyData {

    companion object {
        var email: String = ""
        var userModel: UserModel = UserModel()
        var photoUri: Uri = Uri.EMPTY

        fun init() {
            email = Firebase.auth.currentUser?.email.toString()
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(UserModel::class.java)
                    value?.let {
                        userModel = value
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }
            FirebaseRef.users.child(FirebaseRef.currentUserId).addValueEventListener(postListener)

            val imageRef = Firebase.storage.reference.child("${FirebaseRef.currentUserId}.jpg")
            imageRef.downloadUrl.addOnSuccessListener {
                photoUri = it
            }
        }
    }
}