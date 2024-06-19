package com.bangkit.sostainable.view.main.createReport

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.data.factory.EventModelFactory
import com.bangkit.sostainable.data.json.ReportJson
import com.bangkit.sostainable.data.utils.getImageUri
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.data.utils.reduceFileImage
import com.bangkit.sostainable.data.utils.uriToFile
import com.bangkit.sostainable.databinding.ActivityCreateReportBinding
import com.bangkit.sostainable.view.main.myEvent.MyEventActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CreateReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateReportBinding
    private var currentImageUri: Uri? = null
    private var file: File? = null
    private val fileList = mutableListOf<File>()
    val imageMultipartList = mutableListOf<MultipartBody.Part>()

    private val createReportViewModel by viewModels<CreateReportViewModel> {
        EventModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addImage1.setOnClickListener {
            showImagePickerDialog1()
        }
        binding.addImage2.setOnClickListener {
            showImagePickerDialog2()
        }
        binding.buttonCreateReport.setOnClickListener {
            setupView()
        }
        binding.icBack.setOnClickListener {
            val intent = Intent(this@CreateReportActivity, MyEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    // CREATE REPORT
    private fun setupView(){
        lifecycleScope.launch {
            createReportEvent()
        }
    }

    suspend fun createReportEvent(){
        val kendala = binding.edtKendala.text.toString().trim()
        val jumlah_volunteer = binding.edtJlmVolunteer.text.toString().trim()
        val idEvent = intent.getStringExtra(DATA_EVENT)
        val file = fileList.map { it.reduceFileImage() }
        file.forEach { fileLokasi ->
            val requestImageFile = fileLokasi.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "foto_dokumentasi",
                fileLokasi.name,
                requestImageFile
            )
            imageMultipartList.add(imageMultipart)
        }
        val createReport = ReportJson(kendala, jumlah_volunteer.toInt(),
            idEvent.toString(), imageMultipartList)
        createReportViewModel.createReportEvent(createReport).observe(this, Observer {
            result -> when(result){
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val alertDialogBuilder = AlertDialog.Builder(this@CreateReportActivity)
                    alertDialogBuilder.apply {
                        setTitle("Success")
                        setMessage("Success to create event: ${result.data.message}")
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(this@CreateReportActivity, MyEventActivity::class.java)
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
                    val alertDialogBuilder = AlertDialog.Builder(this@CreateReportActivity)
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

    //IMAGE
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

    private fun startGallery1() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startGallery2() {
        launcherGallery2.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
    }


    companion object {
        const val DATA_EVENT = "data_event"
    }
}