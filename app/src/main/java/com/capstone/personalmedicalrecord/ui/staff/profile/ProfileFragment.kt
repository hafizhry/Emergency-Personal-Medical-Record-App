package com.capstone.personalmedicalrecord.ui.staff.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.personalmedicalrecord.MyPreference
import com.capstone.personalmedicalrecord.R
import com.capstone.personalmedicalrecord.databinding.FragmentStaffProfileBinding
import com.capstone.personalmedicalrecord.ui.login.LoginActivity
import com.capstone.personalmedicalrecord.utils.Utility.navigateTo
import com.capstone.personalmedicalrecord.utils.Utility.searchPatient
import com.capstone.personalmedicalrecord.utils.Utility.searchStaff


class ProfileFragment : Fragment() {
    private lateinit var preference: MyPreference
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentStaffProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preference = MyPreference(requireActivity())
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val staff = preference.getId().searchStaff()
        with(staff) {
            binding.fullName.text = name
            binding.email.text = email
            binding.phoneNumber.text = phoneNumber
            binding.hospital.text = hospital
        }

        binding.logoutBtn.setOnClickListener {
            preference.setId(0)
            preference.setRole("")
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.editProfileBtn.setOnClickListener {
            activity?.navigateTo(UpdateProfileFragment(), R.id.frame)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}