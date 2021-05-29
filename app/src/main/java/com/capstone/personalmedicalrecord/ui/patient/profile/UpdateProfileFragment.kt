package com.capstone.personalmedicalrecord.ui.patient.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.capstone.personalmedicalrecord.MyPreference
import com.capstone.personalmedicalrecord.R
import com.capstone.personalmedicalrecord.core.domain.model.Patient
import com.capstone.personalmedicalrecord.databinding.FragmentPatientUpdateProfileBinding
import com.capstone.personalmedicalrecord.utils.DataDummy
import com.capstone.personalmedicalrecord.utils.Utility.clickBack
import com.capstone.personalmedicalrecord.utils.Utility.convertEmpty
import com.capstone.personalmedicalrecord.utils.Utility.searchPatient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileFragment : Fragment() {
    private lateinit var preference: MyPreference
    private var _binding: FragmentPatientUpdateProfileBinding? = null
    private val binding get() = _binding as FragmentPatientUpdateProfileBinding

    private var photoFile: File? = null
    private var calendar = Calendar.getInstance()
    private var radio = "A"

    private val viewModel: UpdatePatientViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = MyPreference(requireActivity())

        val patient = preference.getId().searchPatient()
        with(patient) {
            binding.inputFullName.setText(name.convertEmpty())
            binding.inputEmail.setText(email.convertEmpty())
            binding.inputAddress.setText(address.convertEmpty())
            binding.inputPhoneNumber.setText(phoneNumber.convertEmpty())
            binding.inputDateBirth.setText(dateBirth.convertEmpty())
            binding.inputGender.setText(gender.convertEmpty())
            binding.inputBloodType.setText(bloodType.convertEmpty())
        }

        setUpDatePicker()
        setBloodType()

        binding.saveChangesBtn.setOnClickListener {
            val id = DataDummy.listPatient.indexOf(patient)
            DataDummy.listPatient[id] = Patient(
                preference.getId(),
                binding.inputFullName.text.toString(),
                binding.inputEmail.text.toString(),
                patient.password,
                binding.inputPhoneNumber.text.toString(),
                binding.inputDateBirth.text.toString(),
                binding.inputAddress.text.toString(),
                binding.inputGender.text.toString(),
                radio.convertEmpty(),
            )
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.changePhoto.setOnClickListener {
            val singleItems = arrayOf("Take a Photo", "Choose a photo")
            var checkedItem = 0

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.add_record_text))
                .setNeutralButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    when (checkedItem) {
                        0 -> takePhoto()
                        1 -> choosePhoto()
                    }
                    Log.d("check", singleItems[checkedItem])
                }
                .setSingleChoiceItems(singleItems, 0) { _, which ->
                    checkedItem = which
                }
                .show()
        }
        activity?.clickBack(binding.backBtn)
    }

    private fun setBloodType() {
        val singleItems = arrayOf("A", "B", "AB", "O")
        var checkedItem = 0
        binding.inputBloodType.setOnClickListener {
            var idx = singleItems.indexOf(binding.inputBloodType.text.toString())
            Log.d("index", idx.toString())
            if (idx == -1) idx = 0

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.add_record_text))
                .setNeutralButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    binding.inputBloodType.setText(singleItems[checkedItem])
                }
                .setSingleChoiceItems(singleItems, idx) { _, which ->
                    checkedItem = which
                }
                .show()
        }
    }

    private fun setUpDatePicker() {
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        binding.inputDateBirth.setOnClickListener {
            val months = DateFormatSymbols.getInstance().months
            val dateNow = binding.inputDateBirth.text.toString()
            Log.d("date", dateNow)
            val splitDate = dateNow.split(" ")
            if (splitDate.size == 3) {
                DatePickerDialog(
                    requireActivity(), date, splitDate[2].toInt(), months.indexOf(splitDate[1]),
                    splitDate[0].toInt()
                ).show()
            } else {
                DatePickerDialog(
                    requireActivity(),
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun updateLabel() {
        val formatter = SimpleDateFormat("d MMMM y", Locale.US)
        binding.inputDateBirth.setText(formatter.format(calendar.time))
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile()
        val providerFile = FileProvider.getUriForFile(
            requireContext(),
            "com.capstone.personalmedicalrecord.fileprovider",
            photoFile as File
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            takePhoto.launch(intent)
        } else {
            Toast.makeText(context, "Camera could not open", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotoFile(): File {
        val storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(FILE_NAME, ".jpg", storageDirectory)
    }

    private fun choosePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                chooseImageGallery()
            }
        } else {
            chooseImageGallery()
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        choosePhoto.launch(intent)
    }

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
//              val takenImage = BitmapFactory.decodeFile(photoFile?.absolutePath)
//              binding.imageView.setImageBitmap(takenImage)
                Glide.with(requireContext())
                    .load(File(photoFile?.absolutePath))
                    .centerCrop()
                    .into(binding.avatar)
            }
        }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                chooseImageGallery()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val choosePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Glide.with(requireContext())
                    .load(result.data?.data)
                    .centerCrop()
                    .into(binding.avatar)
                Toast.makeText(context, result.data?.data.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FILE_NAME = "photo.jpg"
    }
}