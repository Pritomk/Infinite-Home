package com.example.infinitehome.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinitehome.R
import com.example.infinitehome.room.Chat
import com.example.infinitehome.room.UserChat

class ChatFragmentAdapter(private val context: Context, private val listener: ChatFragment) :
    RecyclerView.Adapter<ChatFragmentAdapter.AllChatsViewHolder>() {

    private val chats = ArrayList<UserChat>()

    inner class AllChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userImageAllChats)
        val userName: TextView = itemView.findViewById(R.id.userNameAllChats)
        val userMessage: TextView = itemView.findViewById(R.id.userMessageAllChats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllChatsViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.all_chats_layout, parent, false)
        val viewHolder = AllChatsViewHolder(view)

        view.setOnClickListener {
            listener.onChatItemClicked(chats[viewHolder.adapterPosition].uid)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: AllChatsViewHolder, position: Int) {
        val currentItem = chats[position]
//        holder.userMessage.text = currentItem.message
        holder.userName.text = currentItem.displayName
        Glide.with(context).load(currentItem.imageUrl).centerCrop().into(holder.userImage)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun updateList(newList : List<UserChat>) {
        chats.clear()
        chats.addAll(newList)

        notifyDataSetChanged()
    }

    interface ChatItemClicked {
        fun onChatItemClicked(senderUid: String)
    }
}