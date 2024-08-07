package com.example.hackathonhub

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.hackathonhub.api.RetrofitClient
import com.example.hackathonhub.models.AddHackathonRequest
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Retrofit
class AddHackathonFragment : Fragment() {

    private lateinit var etDescription: EditText
    private lateinit var etLocation: EditText
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var ivProfileImage: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnAddHackathon: Button

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_hackathon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etDescription = view.findViewById(R.id.et_description)
        etLocation = view.findViewById(R.id.et_location)
        etStartDate = view.findViewById(R.id.et_start_date)
        etEndDate = view.findViewById(R.id.et_end_date)
        ivProfileImage = view.findViewById(R.id.iv_profile_image)
        btnSelectImage = view.findViewById(R.id.btn_select_image)
        btnAddHackathon = view.findViewById(R.id.btn_add_hackathon)

        setupDatePickers()

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        btnAddHackathon.setOnClickListener {
            if (validateInput()) {
                val description = etDescription.text.toString().trim()
                val location = etLocation.text.toString().trim()
                val startDate = etStartDate.text.toString().trim()
                val endDate = etEndDate.text.toString().trim()
                val selectedImageUri = (ivProfileImage.drawable as? BitmapDrawable)?.bitmap?.let { bitmap ->
                    getImageUri(requireContext(), bitmap)
                }

                addHackathon(description, location, startDate, endDate, selectedImageUri)
            }
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    ivProfileImage.setImageURI(it)
                }
            }
        }
    }

    private fun setupDatePickers() {
        etStartDate.setOnClickListener {
            showDatePicker { date ->
                etStartDate.setText(date)
            }
        }

        etEndDate.setOnClickListener {
            showDatePicker { date ->
                etEndDate.setText(date)
            }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            onDateSelected(sdf.format(Date(it)))
        }
        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "image.jpg")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return FileProvider.getUriForFile(context, "com.example.hackathonhub.fileprovider", file)
    }

    private fun validateInput(): Boolean {
        val description = etDescription.text.toString().trim()
        val location = etLocation.text.toString().trim()
        val startDate = etStartDate.text.toString().trim()
        val endDate = etEndDate.text.toString().trim()

        var isValid = true;

        if (description.isEmpty()) {
            etDescription.error = "Description is required"
            isValid = false
        }
        if (location.isEmpty()) {
            etLocation.error = "Location is required"
            isValid = false
        }
        if (startDate.isEmpty()) {
            etStartDate.error = "Start date is required"
            isValid = false
        }
        if (endDate.isEmpty()) {
            etEndDate.error = "End date is required"
            isValid = false
        }
        if (startDate > endDate) {
            etEndDate.error = "End date should be after start date"
            isValid = false
        }
        return isValid
    }

    fun getCurrentUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

    private fun addHackathon(description: String, location: String, startDate: String, endDate: String, selectedImageUri: Uri?) {
        val userId = getCurrentUserId(requireContext()) ?: "defaultUserId"

        val hackathon = AddHackathonRequest(
            creator = userId,
            location = location,
            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(startDate) ?: Date(),
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(endDate) ?: Date(),
            description = description,
            comments = listOf(), // Initialize as needed
            imgs = listOf(), // Handle image uploads if necessary
            likes = listOf(),
            dateCreated = Date()
        )

        lifecycleScope.launch {
            try {
                val call = RetrofitClient.apiService.addHackathon(hackathon)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Hackathon added successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to add hackathon: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Log.d("AddHackathonFragment", "Exception: ${e.message}")
            }
        }
    }

}
