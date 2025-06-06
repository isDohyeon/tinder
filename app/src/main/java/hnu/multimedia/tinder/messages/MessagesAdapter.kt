package hnu.multimedia.tinder.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hnu.multimedia.tinder.databinding.ItemMessageBinding

class MessagesAdapter(val list: List<MessageModel>) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemMessageBinding.inflate(inflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val likeUser = list[position]
        holder.binding.textViewSendText.text = likeUser.message

        val imageRef = Firebase.storage.reference.child("${likeUser.senderUid}.jpg")
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.binding.root.context)
                .load(uri)
                .into(holder.binding.imageViewLikeUser)
        }
    }
}