package com.example.infinitehome

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinitehome.databinding.ActivityChatBinding
import com.example.infinitehome.util.chat.ChatActivityAdapter
import com.example.infinitehome.util.chat.ChatActivityViewModel
import com.example.infinitehome.util.chat.ChatActivityViewModelFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var sender: String
    private lateinit var myUid: String
    private lateinit var viewModel: ChatActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var sendBtn: ImageView
    private lateinit var attchBtn: ImageView
    private lateinit var toolbar: Toolbar
    private var senderImage: ImageView? = null
    private var senderName: TextView? = null
    private lateinit var seenTime: TextView
    private var imageUri: Uri? = null
    private lateinit var sendText: TextView
    private val TAG = "com.example.infinitehome.ChatActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sendBtn = binding.sendButton
        attchBtn = binding.attachImage
        toolbar = binding.chatToolbar
        seenTime = binding.seenTextView
        sendText = binding.sendText

        myUid = Firebase.auth.currentUser!!.uid
        sender = intent.getStringExtra("SenderUid").toString()


        Log.e("query_reference",sender)
        viewModel = ViewModelProvider(this,
            ChatActivityViewModelFactory(application,sender))[ChatActivityViewModel::class.java]

        setupRecyclerView()

//        setSupportActionBar(toolbar)

        attchBtn.setOnClickListener {
            imagePicker()
        }

        sendBtn.setOnClickListener {
            sendMessage()
        }
    }

    private fun imagePicker() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            sendText.text = imageUri.toString()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage() {
        if (imageUri != null) {
            viewModel.addImageChat(sender, imageUri!!)
        } else {
            val message = sendText.text.toString()
            viewModel.addTextChat(sender,message)
        }
        sendText.text = ""
    }

    private fun setupRecyclerView() {
        val adapter = ChatActivityAdapter(this)
        recyclerView = binding.chatRecyclerView
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        viewModel.chats.observe(this, { list->
            list?.let {
                Log.d(TAG,it.size.toString())
                it.sortBy { it.timeStamp }
                adapter.updateList(it)
            }
        })
    }

    private fun setToolbar(imageUrl: String, name: String) {
        if (senderImage == null) {
            senderImage = binding.senderUserImage
            Glide.with(this).load(imageUrl).circleCrop().into(senderImage as CircleImageView)
        }
        if (senderName == null) {
            senderName = binding.senderUserName
            senderName!!.text = name
        }
    }

    private fun compareString(str1 : String, str2 : String) : Boolean{
        val comp = str1.compareTo(str2)
        return comp < 0
    }
}