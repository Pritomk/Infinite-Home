package com.example.infinitehome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinitehome.dao.PostDao
import com.example.infinitehome.databinding.ActivityCommentBinding
import com.example.infinitehome.models.Comment
import com.example.infinitehome.repositories.PostRepository
import com.example.infinitehome.util.commet.CommentAdapter
import com.example.infinitehome.util.commet.CommentViewModel
import com.example.infinitehome.util.commet.CommentViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding
    private lateinit var comments: ArrayList<Comment>
    private lateinit var postId: String
    private lateinit var commentViewModel: CommentViewModel
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var addCommentButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postDao = PostDao()
        val postRepository = PostRepository(postDao)
        addCommentButton = binding.addCommentButton
        commentViewModel = ViewModelProvider(this, CommentViewModelFactory(postRepository)).get(
            CommentViewModel::class.java)

        val intent = intent
        comments = intent.getParcelableArrayListExtra<Comment>("CommentExtra") as ArrayList<Comment>

        postId = intent.getStringExtra("PostId")!!

        setupRecyclerView()

        addCommentButton.setOnClickListener {
            commentButtonFunc()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.commentRecyclerView
        commentAdapter = CommentAdapter(this, comments)

        recyclerView.adapter = commentAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun commentButtonFunc() {
        val auth = Firebase.auth
        val user = auth.currentUser!!
        val imageUrl =  user.photoUrl.toString()
        val displayName = user.displayName!!
        val commentText = binding.addCommentEditText.text.toString()

        val comment = Comment(imageUrl,displayName,commentText)

        commentViewModel.addComment(postId,comment).also {

        }

        comments.add(comment)
        commentAdapter.notifyDataSetChanged()
    }

}