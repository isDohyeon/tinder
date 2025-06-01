package hnu.multimedia.tinder.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hnu.multimedia.tinder.auth.UserModel
import hnu.multimedia.tinder.databinding.ItemLikesBinding

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
    }
}