package com.example.infinitehome.util.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinitehome.R
import com.example.infinitehome.room.Chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivityAdapter(val context: Context) : RecyclerView.Adapter<ChatActivityAdapter.ChatViewHolder>() {
    private val LEFT_CHAT_CODE = 101
    private val RIGHT_CHAT_CODE = 102
    private val arrayList = ArrayList<Chat>()

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.chatMessage)
        val image: ImageView = itemView.findViewById(R.id.chatImage)
        val time: TextView = itemView.findViewById(R.id.chatTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if (viewType == RIGHT_CHAT_CODE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.right_chat_layout, parent, false)
            ChatViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.left_chat_layout, parent, false)
            ChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = arrayList[position]
        val timeStamp = chat.timeStamp
        val time = getDate(timeStamp)
        holder.time.text = time
        val type = chat.type
        if (type == "text") {
            holder.message.text = chat.message
            holder.image.visibility = View.GONE
        } else {
            holder.message.visibility = View.GONE
            holder.image.visibility = View.VISIBLE
            setPostImage(chat.message, context, holder.image)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        val myUid = Firebase.auth.currentUser!!.uid
        return if (arrayList[position].sender == myUid) {
            RIGHT_CHAT_CODE
        } else {
            LEFT_CHAT_CODE
        }
    }

    private fun getDate(timeStamp: Long): String {
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp
        return format.format(calendar.time)
    }

    private fun setPostImage(child: String, context: Context, imageView: ImageView) {
        val storage = Firebase.storage
        val reference = storage.reference.child(child)
        reference.downloadUrl.addOnSuccessListener {
            Log.e("image_url_get", it.toString())
            Glide.with(context).load(it).into(imageView)
        }.addOnFailureListener { e ->
            Log.e("image_url_get", "Failed ${e.message} $child")
        }
    }

    fun updateList(newList: ArrayList<Chat>) {
        arrayList.clear()
        arrayList.addAll(newList)

        notifyDataSetChanged()
    }

}