package com.example.submission1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.submission1.databinding.ActivityRegisterBinding
import com.example.submission1.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        authViewModel = ViewModelProvider(this@RegisterActivity).get(AuthViewModel::class.java)

        setListener()

        binding.registerButton.setOnClickListener{
            if(validasi()){
                val name = binding.namaEditText.text.toString().trim()
                val email = binding.emailEditText.text.toString().trim()
                val password =binding.passwordEditText.text.toString().trim()
                authViewModel.isloading.observe(this, {
                    showLoading(it)
                })
                authViewModel.registerLauncher(name, email, password)
                authViewModel.getRegResponse().observe(this,{
                    if (it) {
                        Toast.makeText(this, "Berhasil register", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        showLoading(false)
                        startActivity(intent)
                    }else{
                        showLoading(false)
                        Toast.makeText(this, "Email sudah ada / ada inputan yang tidak sesuai", Toast.LENGTH_SHORT).show()
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                })
            }
        }
    }

    private fun validasi(): Boolean = valiName()

    private fun setListener() {
        with(binding) {
            namaEditText.addTextChangedListener(validasiInput(namaEditText))
        }
    }

    private fun valiName(): Boolean {
        if (binding.namaEditText.text.toString().trim().isEmpty()) {
            binding.namaEditText.error = "Jangan kosong"
            binding.namaEditText.requestFocus()
            return false
        }else{
            return true
        }
    }



    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.GONE
        }
    }

    inner class validasiInput(private val view: View): TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when (view.id) {
                R.id.nama_edit_text ->{
                    valiName()
                }
            }
        }
    }
}