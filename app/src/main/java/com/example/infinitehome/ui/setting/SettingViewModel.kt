package com.example.infinitehome.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {
    private val text = MutableLiveData<String>().apply {
        value = "This is setting fragment"
    }
    val tet : LiveData<String> = text
}