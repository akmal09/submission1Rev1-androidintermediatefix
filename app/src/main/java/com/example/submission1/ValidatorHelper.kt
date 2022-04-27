package com.example.submission1

import android.util.Patterns
import java.util.regex.Pattern

object ValidatorHelper {
    fun cekEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}