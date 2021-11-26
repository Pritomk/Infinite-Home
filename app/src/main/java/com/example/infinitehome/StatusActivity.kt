package com.example.infinitehome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.infinitehome.databinding.ActivityStatusBinding
import com.example.infinitehome.models.Status
import com.example.infinitehome.util.status.StatusActivityViewModel
import com.example.infinitehome.util.status.StatusActivityViewModelFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import jp.shts.android.storiesprogressview.StoriesProgressView


class StatusActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private lateinit var uid: String;
    private var pressTime = 0L
    private val limit = 500L
    private var counter = 0
    private lateinit var storiesProgressView: StoriesProgressView
    private lateinit var statusIV: ImageView
    private lateinit var statusActivityViewModel: StatusActivityViewModel
    private var _binding: ActivityStatusBinding? = null
    private lateinit var reverse: View
    private lateinit var skip: View
    private lateinit var references: ArrayList<Status>
    private val TAG = "com.example.infinitehome.StatusActivity"

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = "VmepFOBFY0Tp1BYYJAE8laJPgqI3"

        statusActivityViewModel = ViewModelProvider(
            this,
            StatusActivityViewModelFactory(uid)
        ).get(StatusActivityViewModel::class.java)

        setupProgressView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storiesProgressView.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storiesProgressView.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    private fun setupProgressView() {
        storiesProgressView = binding.storiesPV
        statusActivityViewModel._references.observe(this, { list ->
            list?.let {
                Log.e(TAG,"Executed")
                references = statusActivityViewModel._references.value!!
                Log.e(TAG,"Executed ${references}")
                setupProgressViewFetchingData(it)
            }
        })
    }

    private fun setupProgressViewFetchingData(list: ArrayList<Status>) {
        val duration = list.size * 2000L
        storiesProgressView.setStoriesCount(list.size)
        storiesProgressView.setStoryDuration(duration)
        storiesProgressView.setStoriesListener(this)
        storiesProgressView.startStories(counter)

        statusIV = binding.statusIV

        setStatusImage(list[counter].imageReference, statusIV)

        reverse = binding.statusReverse

        reverse.setOnClickListener { v ->
            storiesProgressView.reverse()
        }
        reverse.setOnTouchListener(onTouchListener)

        skip = binding.statusSkip

        skip.setOnClickListener { v ->
            storiesProgressView.skip()
        }
        skip.setOnTouchListener(onTouchListener)
    }


    private fun setStatusImage(child: String, imageView: ImageView) {
        val storage = Firebase.storage
        val reference = storage.reference.child(child)
        reference.downloadUrl.addOnSuccessListener {
            Log.e("image_url_get", it.toString())
            Glide.with(this).load(it).into(imageView)
        }.addOnFailureListener { e ->
            Log.e("image_url_get", "Failed ${e.message} $child")
        }
    }

    override fun onNext() {
        setStatusImage(references[++counter].imageReference, statusIV)
    }

    override fun onPrev() {
        if (counter - 1 < 0)
            return
        setStatusImage(references[--counter].imageReference, statusIV)
    }

    override fun onComplete() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        storiesProgressView.destroy()
        super.onDestroy()
    }
}