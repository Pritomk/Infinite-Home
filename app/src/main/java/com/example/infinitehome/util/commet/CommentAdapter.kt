package com.example.infinitehome.util.commet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinitehome.R
import com.example.infinitehome.models.Comment

class CommentAdapter(private val context: Context,private val comments: ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentUserImage : ImageView = itemView.findViewById(R.id.commentUserImage)
        val commentUserName : TextView = itemView.findViewById(R.id.commentLayoutUserName)
        val commentTextView : TextView = itemView.findViewById(R.id.commentLayoutComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.comment_layout,parent,false)
        val viewHolder = CommentViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = comments[position]
        Glide.with(holder.itemView.context).load(currentComment.imageUrl).into(holder.commentUserImage)
        holder.commentUserName.text = currentComment.displayName
        holder.commentTextView.text = currentComment.commentText
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}