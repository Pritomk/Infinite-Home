package com.example.infinitehome.ui.explore

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinitehome.CommentActivity
import com.example.infinitehome.R
import com.example.infinitehome.dao.PostDao
import com.example.infinitehome.databinding.FragmentExploreBinding
import com.example.infinitehome.models.Comment
import com.example.infinitehome.models.Post
import com.example.infinitehome.repositories.PostRepository
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ExploreFragment : Fragment(), IPostAdapter {

    private lateinit var exploreViewModel: ExploreViewModel
    private var _binding: FragmentExploreBinding? = null
    private lateinit var adapter: PostAdapter
    private lateinit var postRepository: PostRepository
    private lateinit var postDao: PostDao
    private lateinit var recyclerView: RecyclerView
    private val TAG = "ExploreFragmentTAG"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        postDao = PostDao()
        postRepository = PostRepository(postDao)

        exploreViewModel =
            ViewModelProvider(
                this,
                ExploreViewModelFactory(postRepository)
            ).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.e("firebase_tag", "checked fragment")

        recyclerView = binding.exploreRecyclerView

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        val postCollection = postDao.postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLikeClicked(postId: String) {
        exploreViewModel.updateLikes(postId)
    }

    override fun onCommentClicked(postId: String, comments: ArrayList<Comment>) {
        val intent = Intent(activity, CommentActivity::class.java)
        intent.putParcelableArrayListExtra("CommentExtra", comments)
        intent.putExtra("PostId", postId)
        startActivity(intent)
    }

    @DelicateCoroutinesApi
    override fun onShareClicked(postId: String) {
        val db = FirebaseFirestore.getInstance()
        val postCollection = db.collection("posts")
        postCollection.document(postId).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data?.get("imageReference")}")
                setPostImage(document.data?.get("imageReference") as String)
            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    private fun setPostImage(child: String) {
        Log.e(TAG,child)
        val storage = Firebase.storage
        val reference = storage.reference.child(child)
        reference.downloadUrl.addOnSuccessListener {
            Log.e("image_url_get", it.toString())
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, it.toString())
            startActivity(Intent.createChooser(intent, "Share image using"));

        }.addOnFailureListener { e ->
            Log.e("image_url_get", "Failed ${e.message} $child")
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}