package com.bangkit.sostainable.view.main.donate

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.EventModelFactory
import com.bangkit.sostainable.data.factory.HistoryDonateFactory
import com.bangkit.sostainable.data.json.DonateJson
import com.bangkit.sostainable.data.local.room.entities.HistoryDonateEvent
import com.bangkit.sostainable.data.local.room.entities.JoinEvent
import com.bangkit.sostainable.data.remote.response.event.detail.Data
import com.bangkit.sostainable.databinding.ActivityDonateBinding
import com.bumptech.glide.Glide
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.view.main.detail.DetailActivity
import com.bangkit.sostainable.view.main.donate.historyDonate.HistoryDonateViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DonateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonateBinding
    private var selectedPaymentMethod: String? = null
    private var tvCurrentDate: String? = null

    private val donateViewModel: DonateViewModel by viewModels {
        EventModelFactory.getInstance(this)
    }
    private val historyDonateViewModel by viewModels<HistoryDonateViewModel> {
        HistoryDonateFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentDate()

        val paymentMethods = listOf(
            PaymentMethod("Digital Wallet", "Dana", R.drawable.iv_dana),
            PaymentMethod("Card Credit / Debit", "Visa, MasterCard, BRI, BCA, Bank Indonesia", R.drawable.ic_card)
        )

        val bundle = intent.getBundleExtra(EXTRA_DONATION)
        val id = bundle?.getString("id_event")
        val imageUrl = bundle?.getString("imageUrl")
        val title = bundle?.getString("title")
        val description = bundle?.getString("description")

        binding.tvTitle.text = title
        binding.tvDesc.text = description
        Glide.with(this)
            .load(imageUrl)
            .into(binding.imageView)

        radioButtons(paymentMethods)
        payment()

        binding.buttonDonate.setOnClickListener{
            val sumDonate = binding.formDonate.editText?.text.toString()
            val donateJson = DonateJson(id_event = id.toString(), tanggal = tvCurrentDate.toString(), nominal = sumDonate.toLong())
            lifecycleScope.launch {
                donateEvent(donateJson)
            }
            addHistoryDonate(id.toString(), imageUrl.toString(), title.toString(), tvCurrentDate.toString(), selectedPaymentMethod.toString(), sumDonate)
            val intent = Intent(this, DetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(DetailActivity.DATA_EVENT, id)
            startActivity(intent)
        }
        binding.icBack.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(DetailActivity.DATA_EVENT, id)
            startActivity(intent)
        }
    }

    private fun addHistoryDonate(id: String, imageUrl: String, title: String, tvCurrentDate: String, payment: String, nominal: String) {
        val historyDonateEvent = HistoryDonateEvent(
            idEvent = id,
            imageUrl = imageUrl,
            title = title,
            Date = tvCurrentDate,
            payment = payment,
            nominal = nominal
        )
        historyDonateViewModel.insertHistoryDonate(historyDonateEvent)
    }

    private fun currentDate(){
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        tvCurrentDate = currentDate
    }

    private suspend fun donateEvent(donateJson: DonateJson){
        donateViewModel.donateEvent(donateJson).observe(this, Observer{ result ->
            when(result){
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val alertDialogBuilder = AlertDialog.Builder(this@DonateActivity)
                    alertDialogBuilder.apply {
                        setTitle("SUCCESS")
                        setMessage("SUCCESS to donate event: ${title}, with payment method: ${selectedPaymentMethod}")
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    val alertDialogBuilder = AlertDialog.Builder(this@DonateActivity)
                    alertDialogBuilder.apply {
                        setTitle("FAILED")
                        setMessage("FAILED to donate event: ${title}, with error message: ${result.error}")
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun radioButtons(paymentMethods: List<PaymentMethod>) {
        for (paymentMethod in paymentMethods) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_radio_button, binding.rgPaymentMethods, false)

            val icon = itemView.findViewById<ImageView>(R.id.iv_payment_icon)
            val title = itemView.findViewById<TextView>(R.id.tv_payment_title)
            val subtitle = itemView.findViewById<TextView>(R.id.tv_payment_subtitle)
            val radioButton = itemView.findViewById<RadioButton>(R.id.rb_payment)

            icon.setImageDrawable(ContextCompat.getDrawable(this, paymentMethod.iconRes))
            title.text = paymentMethod.title
            subtitle.text = paymentMethod.subtitle

            radioButton.id = View.generateViewId()
            radioButton.setOnClickListener {
                Toast.makeText(this, "Selected: ${paymentMethod.title}", Toast.LENGTH_SHORT).show()
                selectedPaymentMethod = paymentMethod.title
                binding.rgPaymentMethods.check(radioButton.id)
            }
            binding.rgPaymentMethods.addView(itemView)
        }
    }

    private fun payment(){
        binding.rgPaymentMethods.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val itemView = group.getChildAt(i)
                val radioButton = itemView.findViewById<RadioButton>(R.id.rb_payment)
                if (radioButton != null) {
                    radioButton.isChecked = radioButton.id == checkedId
                }
            }
        }
    }

    companion object{
        const val EXTRA_DONATION = "DATA_DONATION"
    }

    data class PaymentMethod(val title: String, val subtitle: String, val iconRes: Int)
}