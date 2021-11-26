package com.example.infinitehome.ui.status

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinitehome.R
import com.example.infinitehome.models.User

class StatusRecyclerViewAdapter(private val listener: StatusItemClicked) : RecyclerView.Adapter<StatusViewHolder>() {

    private val users : ArrayList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.status_item,parent,false)
        val viewHolder = StatusViewHolder(view)
        view.setOnClickListener {
            listener.onClicked(users[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val currentItem = users[position]
        holder.statusName.text = currentItem.displayName
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.statusImage)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateStatus(updateUsers : ArrayList<User>) {
        users.clear()
        users.addAll(updateUsers)

        notifyDataSetChanged()
    }


}

class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val statusName : TextView = itemView.findViewById(R.id.statusListNameTV)
    val statusImage : ImageView = itemView.findViewById(R.id.statusListProfileIV)
}

interface StatusItemClicked {
    fun onClicked(item: User)
}