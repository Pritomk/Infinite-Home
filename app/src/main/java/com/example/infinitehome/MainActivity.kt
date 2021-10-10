package com.example.infinitehome

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.infinitehome.databinding.ActivityMainBinding
import com.example.infinitehome.ui.chat.ChatViewModel
import com.example.infinitehome.ui.chat.ChatViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fab : FloatingActionButton
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fab = binding.fab

        binding.navView.background = null
        binding.navView.menu[2].isEnabled = false

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_explore, R.id.navigation_try_ar, R.id.navigation_chat,R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        chatViewModel = ViewModelProvider(this,ChatViewModelFactory(application))[ChatViewModel::class.java]


        fab.setOnClickListener {
            startActivity(Intent(this,CreatePostActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        chatViewModel.readChats(application)
    }
}