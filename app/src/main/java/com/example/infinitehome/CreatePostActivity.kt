package com.example.infinitehome

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.infinitehome.dao.PostDao
import com.example.infinitehome.databinding.ActivityCreatePostBinding
import com.example.infinitehome.models.Post
import com.example.infinitehome.repositories.PostRepository
import com.example.infinitehome.ui.explore.ExploreViewModel
import com.example.infinitehome.ui.explore.ExploreViewModelFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*


class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var createPostImageView: ImageView
    private lateinit var createPostEditText: EditText
    private lateinit var createPostButton: Button
    private lateinit var imageUri: Uri
    private lateinit var user: FirebaseUser
    private lateinit var exploreViewModel: ExploreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postDao = PostDao()
        val postRepository = PostRepository(postDao)

        exploreViewModel = ViewModelProvider(this,ExploreViewModelFactory(postRepository)).get(ExploreViewModel::class.java)

        createPostImageView = binding.createPostImageView
        createPostEditText = binding.createPostEditText
        createPostButton = binding.createPostButton

        user = FirebaseAuth.getInstance().currentUser!!

        createPostImageView.setOnClickListener {
            imagePicker()
        }

        createPostButton.setOnClickListener {
            postButtonAction()
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
            Glide.with(this).load(imageUri).into(createPostImageView)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun postButtonAction() {
        val text = createPostEditText.text.toString()
        if (text.isNotEmpty()) {
            exploreViewModel.addPost(this,imageUri,text)
            Toast.makeText(this,"Successfully uploaded...",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

//    private fun getImage(child : String) {
//        val storage = Firebase.storage
//        val reference = storage.reference.child(child)
//        reference.downloadUrl.addOnSuccessListener {
//            Log.e("image_url_get",it.toString())
//            Glide.with(this).load(it).into(createPostImageView)
//        }.addOnFailureListener {e ->
//            Log.e("image_url_get","Failed ${e.message} $child")
//            Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT)
//                .show()
//        }
//    }


}