package com.bangkit.sostainable.view.main.create

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.sostainable.databinding.ActivityCreateBinding
import com.bangkit.sostainable.view.main.MainActivity
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivityCreateBinding
    private val calender = Calendar.getInstance()
    private var includeLocation: Boolean = false
    private var location: LocationManager? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        binding.locationCheckbox.setOnCheckedChangeListener { _, isChecked ->
            includeLocation = isChecked
            if (includeLocation) {
                checkLocationPermission()
            } else {
                binding.formLocation.editText?.setText("")
                stopLocationUpdates()
            }
        }

        binding.icBack.setOnClickListener {
            val intent = Intent(this@CreateActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.icStartDate.setOnClickListener{
            showStartDatePicker()
        }

        binding.icEndDate.setOnClickListener{
            showEndDatePicker()
        }
    }

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

    private fun showStartDatePicker(){
        val datePicker = DatePickerDialog(this@CreateActivity,{DatePicker,
                year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year,month,dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formateDate = dateFormat.format(selectedDate.time)
            binding.formStartdate.editText?.setText(formateDate)
        },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showEndDatePicker(){
        val datePicker = DatePickerDialog(this@CreateActivity,{DatePicker,
                                                               year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year,month,dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formateDate = dateFormat.format(selectedDate.time)
            binding.formEndDate.editText?.setText(formateDate)
        },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}