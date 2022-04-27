package com.example.submission1

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText : AppCompatEditText, View.OnTouchListener {

    var returnPassword :Boolean = false
    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.length < 6) {
                    error = "password kurang dari 6"
                }else if (p0.toString().isEmpty()) {
                    error = "password harus diisi dan minimal 6"
                }else{
                    returnPassword = true
                }
            }

        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

    fun checkPassword():Boolean {
        return returnPassword
    }

//    private fun valiPassword(): Boolean {
//        if (binding.passwordEditText.text.toString().trim().isEmpty()) {
//            binding.passwordEditText.error =  getString(R.string.emptyAlert)
//            binding.passwordEditText.requestFocus()
//            return false
//        }else if (binding.passwordEditText.text.toString().length < 6) {
//            binding.passwordEditText.error =  getString(R.string.passworAlert)
//            binding.passwordEditText.requestFocus()
//            return false
//        }else{
//            return true
//        }
//    }
}