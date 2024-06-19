package com.bangkit.sostainable.view.main.create

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.data.factory.EventModelFactory
import com.bangkit.sostainable.data.repository.event.Event
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.data.utils.getImageUri
import com.bangkit.sostainable.data.utils.reduceFileImage
import com.bangkit.sostainable.data.utils.uriToFile
import com.bangkit.sostainable.databinding.ActivityCreateBinding
import com.bangkit.sostainable.view.main.MainActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivityCreateBinding
    private var currentImageUri: Uri? = null
    private var file: File? = null
    private val fileList = mutableListOf<File>()
    val imageMultipartList = mutableListOf<MultipartBody.Part>()

    private val calender = Calendar.getInstance()
    private var includeLocation: Boolean = false
    private var location: LocationManager? = null
    private val createViewModel: CreateViewModel by viewModels {
        EventModelFactory.getInstance(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
        backButton()

        binding.addImage1.setOnClickListener {
            showImagePickerDialog1()
        }
        binding.addImage2.setOnClickListener {
            showImagePickerDialog2()
        }
        binding.addImage3.setOnClickListener {
            showImagePickerDialog3()
        }
        binding.icStartDate.setOnClickListener{
            showStartDatePicker()
        }
        binding.icEndDate.setOnClickListener{
            showEndDatePicker()
        }
        binding.icStartTime.setOnClickListener{
            showStartTimePickerDialog()
        }

        binding.icEndTime.setOnClickListener{
            showEndTimePickerDialog()
        }

        binding.buttonDonate.setOnClickListener {
            lifecycleScope.launch {
                createEvent()
            }
        }
    }


    // CREATE EVENT
    private suspend fun createEvent(){
        val judulEvent = binding.edtTitle.text.toString().trim()
        val descEvent = binding.edtDescription.text.toString().trim()
        val startDate = binding.edtStartDate.text.toString().trim()
        val endDate = binding.edtEndDate.text.toString().trim()
        val donate = binding.edtDonate.text.toString().trim()
        val volunteer = binding.edtVolunteer.text.toString().trim()
        val location = binding.edtLocation.text.toString().trim()
        val tipeLocation = binding.edtTipeLokasi.text.toString().trim()
        val startTime = binding.edtStartTime.text.toString().trim()
        val endTime = binding.edtEndTime.text.toString().trim()
        val file = fileList.map { it.reduceFileImage() }
        file.forEach { fileLokasi ->
            val requestImageFile = fileLokasi.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file_foto",
                fileLokasi.name,
                requestImageFile
            )
            imageMultipartList.add(imageMultipart)
        }
        imageMultipartList.forEach {
            Log.e("CreateActivity", "createEvent: $it")
            Log.e("CreateActivity", "createEvent: $imageMultipartList")
        }
        val createEvent = Event(judulEvent = judulEvent, tipeLokasi = tipeLocation, fileFoto = imageMultipartList, deskripsiEvent = descEvent, jamMulai = startTime, jamSelesai = endTime, tanggalMulai = startDate, tanggalSelesai = endDate, alamat = location, jumlahMinimumVolunteer = volunteer.toInt(), jumlahMinimumDonasi = donate.toLong(), pembuatEvent = "sss")
        createViewModel.createEvent(createEvent).observe(this, Observer {
            result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val alertDialogBuilder = AlertDialog.Builder(this@CreateActivity)
                    alertDialogBuilder.apply {
                        setTitle("Success")
                        setMessage("Success to create event: ${result.data.message}")
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(this@CreateActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    val alertDialogBuilder = AlertDialog.Builder(this@CreateActivity)
                    alertDialogBuilder.apply {
                        setTitle("Error")
                        setMessage("Failed to create event: ${result.error}")
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                    clearLists()
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
        })
    }

    // IMAGE
    private fun showImagePickerDialog1() {
        val options = arrayOf("Kamera", "Galeri")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Sumber Gambar")

        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> startCamera1()
                1 -> startGallery1()
            }
        }

        builder.show()
    }

    private fun showImagePickerDialog2() {
        val options = arrayOf("Kamera", "Galeri")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Sumber Gambar")

        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> startCamera2()
                1 -> startGallery2()
            }
        }

        builder.show()
    }

    private fun showImagePickerDialog3() {
        val options = arrayOf("Kamera", "Galeri")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Sumber Gambar")

        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> startCamera3()
                1 -> startGallery3()
            }
        }

        builder.show()
    }

    private fun startGallery1() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startGallery2() {
        launcherGallery2.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startGallery3() {
        launcherGallery3.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                file = uriToFile(it!!, this)
                addToFileList(file)
            }
            showImage1()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherGallery2 = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                file = uriToFile(it!!, this)
                addToFileList(file)
            }
            showImage2()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherGallery3 = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                file = uriToFile(it!!, this)
                addToFileList(file)
            }
            showImage3()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera1() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri.let {
                file = uriToFile(it!!, this)
                addToFileList(file)
            }
            showImage1()
        }
    }
    private fun showImage1() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivImage1.setImageURI(it)
        }
    }

    private fun startCamera2() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera2.launch(currentImageUri!!)
    }
    private val launcherIntentCamera2 = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri.let {
                file = uriToFile(it!!, this)
                addToFileList(file)
            }
            showImage2()
        }
    }
    private fun showImage2() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivImage2.setImageURI(it)
        }
    }

    private fun startCamera3() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera3.launch(currentImageUri!!)
    }
    private val launcherIntentCamera3 = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri.let {
                file = uriToFile(it!!, this)
                addToFileList(file)
            }
            showImage3()
        }
    }
    private fun showImage3() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivImage3.setImageURI(it)
        }
    }

    private fun addToFileList(file: File?) {
        file?.let {
            fileList.add(it)
        }
    }

    private fun clearLists() {
        fileList.clear()
        imageMultipartList.clear()
        binding.ivImage1.setImageDrawable(null)
        binding.ivImage2.setImageDrawable(null)
        binding.ivImage3.setImageDrawable(null)
    }

    private fun backButton(){
        binding.icBack.setOnClickListener {
            val intent = Intent(this@CreateActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun checkLocation(){
        binding.locationCheckbox.setOnCheckedChangeListener { _, isChecked ->
            includeLocation = isChecked
            if (includeLocation) {
                checkLocationPermission()
            } else {
                binding.formLocation.editText?.setText("")
                stopLocationUpdates()
            }
        }
    }


    // LOCATION
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            getLocation()
        }
    }

    private fun getLocation() {
        try {
            location?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun stopLocationUpdates() {
        try {
            location?.removeUpdates(this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses[0]
                    val addressText = address.getAddressLine(0)
                    binding.formLocation.editText?.setText(addressText)
                } else {
                    binding.formLocation.editText?.setText("")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            binding.formLocation.editText?.setText("")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // DATE
    private fun showStartDatePicker() {
        val datePicker = DatePickerDialog(this@CreateActivity, { _, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.formStartdate.editText?.setText(formattedDate)
        },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showEndDatePicker() {
        val datePicker = DatePickerDialog(this@CreateActivity, { _, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.formEndDate.editText?.setText(formattedDate)
        },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }


    // TIME
    private fun showStartTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.formStartTime.editText?.setText(formattedTime)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun showEndTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.formEndTime.editText?.setText(formattedTime)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}