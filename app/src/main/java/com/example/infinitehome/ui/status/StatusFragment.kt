package com.example.infinitehome.ui.status

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitehome.CreatePostActivity
import com.example.infinitehome.StatusActivity
import com.example.infinitehome.databinding.FragmentStatusBinding
import com.example.infinitehome.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StatusFragment : Fragment(), StatusItemClicked {

    private lateinit var statusViewModel: StatusViewModel
    private var _binding: FragmentStatusBinding? = null
    private lateinit var fab: FloatingActionButton
    private lateinit var statusRV: RecyclerView
    private val TAG = "com.example.infinitehome.ui.status.StatusFragment"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val uId = Firebase.auth.currentUser!!.uid
        statusViewModel =
            ViewModelProvider(this,StatusViewModelFactory(uId)).get(StatusViewModel::class.java)

        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = binding.addStatusFab
        statusRV = binding.statusRV
//        var btn = binding.demoBtn
//        btn.setOnClickListener {
//            startActivity(Intent(requireContext(),StatusActivity::class.java))
//        }

        //Setup recycler view
        setupStatusRV()

        fab.setOnClickListener {
            addStatusFunc()
        }
    }

    private fun setupStatusRV() {
        val statusAdapter = StatusRecyclerViewAdapter(this)
        statusRV.adapter = statusAdapter
        statusRV.layoutManager = LinearLayoutManager(activity)
        Log.d(TAG,"size is")
        statusViewModel._userIds.observe(viewLifecycleOwner,{
            Log.d(TAG,"size is ${it.size}")
            statusAdapter.updateStatus(it)
        })
    }

    private fun addStatusFunc() {
        val intent = Intent(requireContext(),CreatePostActivity::class.java)
        intent.putExtra("from_fragment","StatusFragment");
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClicked(item: User) {
        startActivity(Intent(requireContext(),StatusActivity::class.java).putExtra("uid",item.uid))
    }
}