package com.example.infinitehome.ui.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinitehome.ChatActivity
import com.example.infinitehome.databinding.FragmentChatBinding
import com.example.infinitehome.room.Chat
import com.google.firebase.firestore.FirebaseFirestore

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatViewModel =
            ViewModelProvider(this,ChatViewModelFactory(requireActivity().application)).get(ChatViewModel::class.java)

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = ChatFragmentAdapter(requireContext(),this)
        val recyclerView = binding.recyclerViewChatFragment
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.hasFixedSize()

        chatViewModel.allChats.observe(viewLifecycleOwner, { list ->
            list?.let {list->
                list.sortedBy { it.displayName }
                adapter.updateList(list)
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onChatItemClicked(senderUid: String) {
        val intent = Intent(activity,ChatActivity::class.java)
        intent.putExtra("SenderUid",senderUid)
        Log.d("query_reference",senderUid)
        startActivity(intent)
    }


}