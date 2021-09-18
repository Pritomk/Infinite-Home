package com.example.infinitehome.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.infinitehome.dao.PostDao
import com.example.infinitehome.databinding.FragmentExploreBinding
import com.example.infinitehome.repositories.PostRepository

class ExploreFragment : Fragment() {

    private lateinit var exploreViewModel: ExploreViewModel
    private var _binding: FragmentExploreBinding? = null
//    private lateinit var adapter: PostAdapter
    private lateinit var postRepository: PostRepository
    private lateinit var postDao : PostDao

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
            ViewModelProvider(this,ExploreViewModelFactory(postRepository)).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val recyclerView = binding.exploreRecyclerView
//        val postCollection = FirebaseFirestore.getInstance().collection("posts")
//        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
//        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
//
//        adapter = PostAdapter(recyclerViewOptions,this)
//        recyclerView.adapter = adapter
//
//        recyclerView.layoutManager = LinearLayoutManager(activity)

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    fun onLikeClicked(id: String) {
//        exploreViewModel.updateLikes(id)
//    }
}