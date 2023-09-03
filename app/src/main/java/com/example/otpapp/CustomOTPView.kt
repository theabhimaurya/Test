package com.example.otpapp

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.example.otpapp.databinding.CustomOtpViewBinding // Replace with your actual package and binding class name

class CustomOTPView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val editTexts = mutableListOf<EditText>()

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_otp_view, this, true)

        editTexts.add(findViewById(R.id.otpEditText1))
        editTexts.add(findViewById(R.id.otpEditText2))
        editTexts.add(findViewById(R.id.otpEditText3))
        editTexts.add(findViewById(R.id.otpEditText4))

        setupEditTextListeners()
    }

    private fun setupEditTextListeners() {
        for (i in 0 until editTexts.size) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        if (i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    } else {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    }
                }
            })
        }
    }
}
