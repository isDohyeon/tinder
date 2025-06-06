package hnu.multimedia.tinder.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hnu.multimedia.tinder.databinding.ActivityMessagesBinding
import hnu.multimedia.tinder.util.FirebaseRef

class MessagesActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMessagesBinding.inflate(layoutInflater) }
    private val messages = mutableListOf<MessageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getMessages()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MessagesAdapter(messages)
    }

    private fun getMessages() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (dataSnapshot in snapshot.children) {
                    val value = dataSnapshot.getValue(MessageModel::class.java)
                    value?.let {
                        messages.add(value)
                    }
                }
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        FirebaseRef.messages.child(FirebaseRef.currentUserId).addValueEventListener(postListener)
    }
}