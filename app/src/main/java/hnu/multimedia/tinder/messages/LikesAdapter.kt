package hnu.multimedia.tinder.messages

import android.app.AlertDialog
import android.view.LayoutInflater
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
import hnu.multimedia.tinder.databinding.DialogMessageBinding
import hnu.multimedia.tinder.databinding.ItemLikesBinding
import hnu.multimedia.tinder.util.FirebaseRef
import hnu.multimedia.tinder.util.MyData
import hnu.multimedia.tinder.util.MyFirebaseMessagingSender

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
            val title = "${likeUser.nickName}에게 메시지 보내기"
            val bindingDialog =
                DialogMessageBinding.inflate(LayoutInflater.from(holder.binding.root.context))
            val builder = AlertDialog.Builder(holder.binding.root.context)
                .setView(bindingDialog.root)
                .setTitle(title)
            val dialog = builder.show()
            bindingDialog.buttonSend.setOnClickListener {
                Snackbar.make(holder.binding.root, "메시지를 보냈습니다!", Snackbar.LENGTH_LONG).show()
                val message = "${MyData.userModel.nickName} : ${bindingDialog.editTextMessage.text}"
                FirebaseRef.messages.child(list[position].uid).push().setValue(MessageModel(FirebaseRef.currentUserId, message))
                val messageTitle = "${MyData.userModel.nickName}님이 메시지를 보냈어요!"
                MyFirebaseMessagingSender().sendFCM(likeUser.uid, messageTitle, message)
                dialog.dismiss()
            }
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