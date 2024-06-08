package com.bangkit.sostainable.view.main.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.sostainable.R
import com.bangkit.sostainable.databinding.ActivityDonateBinding

class DonateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val paymentMethods = listOf(
            PaymentMethod("Dompet Digital", "Dana", R.drawable.iv_dana),
            PaymentMethod("Kartu kredit atau debit", "Visa, MasterCard, BRI, BCA, Bank Indonesia", R.drawable.ic_card)
        )

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
                binding.rgPaymentMethods.check(radioButton.id)
            }
            binding.rgPaymentMethods.addView(itemView)
        }

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

    data class PaymentMethod(val title: String, val subtitle: String, val iconRes: Int)
}