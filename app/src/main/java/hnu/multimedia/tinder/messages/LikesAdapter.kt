package hnu.multimedia.tinder.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hnu.multimedia.tinder.auth.UserModel
import hnu.multimedia.tinder.databinding.ItemLikesBinding
import hnu.multimedia.tinder.util.FirebaseRef
import hnu.multimedia.tinder.util.MyData
import hnu.multimedia.tinder.util.MyFirebaseMessagingSender
import hnu.multimedia.tinder.util.NotificationUtil

class LikesAdapter(val list: List<UserModel>) : RecyclerView.Adapter<LikesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemLikesBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemLikesBinding.inflate(inflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val likeUser = list[position]
        holder.binding.textViewNickname.text = likeUser.nickName
        holder.binding.textViewAge2.text = likeUser.age.toString()

        val imageRef = Firebase.storage.reference.child("${likeUser.uid}.jpg")
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.binding.root.context)
                .load(uri)
                .into(holder.binding.imageViewPhoto)
        }

        likeMe(list[position].uid, holder)

        holder.binding.root.setOnClickListener {
            val body = "${MyData.userModel.nickName}가 당신을 좋아합니다!"
            MyFirebaseMessagingSender().sendFCM(likeUser.uid, "I like you!", body)
            Snackbar.make(holder.binding.root, "메시지를 보냈습니다!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun likeMe(likeUid: String, holder: ViewHolder) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue<Boolean>() == true) {
                    holder.binding.imageViewLike.isVisible = true
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        FirebaseRef.likes.child(likeUid).child(FirebaseRef.currentUserId).addValueEventListener(postListener)
    }

}