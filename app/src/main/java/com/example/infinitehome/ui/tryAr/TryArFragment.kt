package com.example.infinitehome.ui.tryAr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.infinitehome.databinding.FragmentTryArBinding

class TryArFragment : Fragment() {

    private lateinit var tryArViewModel: TryArViewModel
    private var _binding: FragmentTryArBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tryArViewModel =
            ViewModelProvider(this).get(TryArViewModel::class.java)

        _binding = FragmentTryArBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tryArViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}