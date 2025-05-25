package hnu.multimedia.tinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hnu.multimedia.tinder.databinding.ItemCardBinding

class CardStackAdapter(val list: List<String>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

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
        holder.binding.textView.text = list[position]
    }
}