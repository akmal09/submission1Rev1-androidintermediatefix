package com.example.submission1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.submission1.databinding.ActivityLoginBinding
import com.example.submission1.model.LoginSession
import com.example.submission1.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var loginSession: LoginSession
    private lateinit var mSessionPreference: SessionPreference
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var emailEditText: EmailEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        mSessionPreference = SessionPreference(this)
        showLoading(false)
        passwordEditText = PasswordEditText(this)
        emailEditText = EmailEditText(this)

        setUpLanguage()

        if (mSessionPreference.getSession().name != "") {
            val newLoginSession= mSessionPreference.getSession()
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra(HomeActivity.EXTRA_RESULT, newLoginSession)
            startActivity(intent)
            finish()
        }

        authViewModel =ViewModelProvider(this@LoginActivity).get(AuthViewModel::class.java)

        binding.loginButton.setOnClickListener{
            Log.d("loginactivitu","${passwordEditText.error}, ${emailEditText.error}")
            if (passwordEditText.error==null&&emailEditText.error==null) {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()
                authViewModel.loginLauncher(email, password)
                authViewModel.isloading.observe(this,{
                    showLoading(it)
                })
                authViewModel.getLogResponse().observe(this, {
                    if (it) {
                        authViewModel.getLoginData().observe(this, {
                            saveSession(it.name,it.token,it.userId)
                        })
                    }else{
                        showLoading(false)
                        Toast.makeText(this, "GAGAL", Toast.LENGTH_SHORT).show()
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                })
            }else{
                Toast.makeText(this, "Mohon isi email dan password nya", Toast.LENGTH_SHORT).show()
            }
        }
        binding.registerIntent.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpLanguage() {
        binding.setLanguage.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerIntent, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playTogether(email, emailEditText)
            startDelay = 500
        }.start()

        AnimatorSet().apply {
            playTogether(password, passwordEditText)
            startDelay = 500
        }.start()

        AnimatorSet().apply{
            playSequentially(
                loginButton,
                register
            )
            startDelay = 500
        }.start()
        ObjectAnimator.ofFloat(binding.registerIntent, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun saveSession(name: String, token: String, userId: String) {
        mSessionPreference = SessionPreference(this)

        loginSession = LoginSession()
        loginSession.name = name
        loginSession.token = token
        loginSession.userId = userId

        mSessionPreference.setSession(loginSession)
        moveToHome()
    }

    private fun moveToHome() {
        mSessionPreference = SessionPreference(this)
        val newLoginSession= mSessionPreference.getSession()
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        Toast.makeText(this, "validated", Toast.LENGTH_SHORT).show()
        intent.putExtra(HomeActivity.EXTRA_RESULT, newLoginSession)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.GONE
        }
    }
}