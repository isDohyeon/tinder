package hnu.multimedia.tinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hnu.multimedia.tinder.auth.UserModel
import hnu.multimedia.tinder.databinding.ItemCardBinding

class CardStackAdapter(val list: List<UserModel>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemCardBinding.inflate(inflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        holder.binding.textViewNick.text = user.nickName
        holder.binding.textViewCity.text = user.city
        holder.binding.textViewAge.text = user.age.toString()

        val imageRef = Firebase.storage.reference.child("${user.uid}.jpg")
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.binding.root.context)
                .load(uri)
                .into(holder.binding.imageViewPhoto2)
        }
    }
}