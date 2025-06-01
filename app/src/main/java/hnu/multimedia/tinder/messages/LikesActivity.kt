package hnu.multimedia.tinder.messages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hnu.multimedia.tinder.auth.UserModel
import hnu.multimedia.tinder.databinding.ActivityLikesBinding
import hnu.multimedia.tinder.util.FirebaseRef

class LikesActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLikesBinding.inflate(layoutInflater) }
    private val likes = mutableListOf<String>()
    private val likedUsers = mutableListOf<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getLikes()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = LikesAdapter(likedUsers)
    }

    private fun getLikes() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                likes.clear()
                for (dataModel in snapshot.children) {
                    val key = dataModel.key.toString()
                    likes.add(key)
                }
                Log.d("LikesActivity", "getLikes: ${likes}")
//                binding.cardStackView.adapter?.notifyDataSetChanged()
                getLikedUsers()
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        FirebaseRef.likes.child(FirebaseRef.currentUserId).addValueEventListener(postListener)
    }

    private fun getLikedUsers() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                likedUsers.clear()
                for (dataModel in snapshot.children) {
                    val value = dataModel.getValue(UserModel::class.java)
                    value?.let {
                        if (likes.contains(it.uid)) {
                            likedUsers.add(it)
                        }
                    }
                }
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        FirebaseRef.users.addValueEventListener(postListener)
    }
}