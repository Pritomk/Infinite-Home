package com.example.infinitehome.ui.explore

//import android.content.Context
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.infinitehome.R
//import com.example.infinitehome.models.Post
//import com.example.infinitehome.util.TimeConverter
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.ktx.storage
//import kotlinx.android.synthetic.main.activity_create_post.view.*
//import kotlinx.android.synthetic.main.post_layout.view.*
//
//class PostAdapter(options: FirestoreRecyclerOptions<Post>, private val listener: ExploreFragment) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {
//
//    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
//        val userImage : ImageView = itemView.createPostImageView
//        val userName : TextView = itemView.explorePostUserName
//        val postImage : ImageView = itemView.explorePostImage
//        val postTime : TextView = itemView.explorePostTime
//        val likeCount : TextView = itemView.explorePostLikeCount
//        val likeButton : ImageView = itemView.explorePostLikeButton
//        val commentButton : ImageView = itemView.explorePostCommentButton
//        val shareButton : ImageView = itemView.explorePostShareButton
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//        val viewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent ,false))
//        viewHolder.likeButton.setOnClickListener {
//            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
//        }
//        return viewHolder
//    }
//
//    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
//        holder.userName.text = model.createdBy.displayName
//        holder.postTime.text = TimeConverter.getTimeAgo(model.createdAt)
//        holder.likeCount.text = model.likedBy.size.toString()
//        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
//        getImage(model.imageReference,holder.postImage.context,holder.postImage)
//
//        val auth = Firebase.auth
//        val currentUserId = auth.currentUser!!.uid
//        val isLiked = model.likedBy.contains(currentUserId)
//
//        if (isLiked) {
//            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.ic_like))
//        } else {
//            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.ic_unlike))
//        }
//    }
//
//        private fun getImage(child : String, context : Context,imageView: ImageView) {
//        val storage = Firebase.storage
//        val reference = storage.reference.child(child)
//        reference.downloadUrl.addOnSuccessListener {
//            Log.e("image_url_get",it.toString())
//            Glide.with(context).load(it).into(imageView)
//        }.addOnFailureListener {e ->
//            Log.e("image_url_get","Failed ${e.message} $child")
//        }
//    }
//}
//
//interface IPostAdapter {
//    fun onLikeClicked(postId : String)
//}