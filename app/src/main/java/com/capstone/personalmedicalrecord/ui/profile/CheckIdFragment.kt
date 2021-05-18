package com.capstone.personalmedicalrecord.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.personalmedicalrecord.databinding.FragmentCheckIdBinding

class CheckIdFragment : Fragment() {

    private var _binding: FragmentCheckIdBinding? = null
    private val binding get() = _binding as FragmentCheckIdBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckIdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}