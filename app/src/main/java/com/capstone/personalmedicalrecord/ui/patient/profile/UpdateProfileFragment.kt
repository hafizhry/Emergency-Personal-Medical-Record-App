package com.capstone.personalmedicalrecord.ui.patient.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.personalmedicalrecord.databinding.FragmentPatientUpdateProfileBinding
import com.capstone.personalmedicalrecord.utils.Utility.clickBack

class UpdateProfileFragment : Fragment() {

    private var _binding: FragmentPatientUpdateProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveChangesBtn.setOnClickListener {
            saveChanges()
        }
        activity?.clickBack(binding.backBtn)
    }

    private fun saveChanges() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}