package com.ramaqq.storyapp_submission1.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ramaqq.storyapp_submission1.R
import com.ramaqq.storyapp_submission1.databinding.ActivityLoginBinding
import com.ramaqq.storyapp_submission1.databinding.ActivityRegisterBinding
import com.ramaqq.storyapp_submission1.ui.login.LoginActivity
import com.ramaqq.storyapp_submission1.ui.login.LoginViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: RegisterViewModel by viewModels()
        setMyButton()
        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
               setMyButton()
                if (p0.isEmpty()) binding.edtUsername.error = getString(R.string.username_hint)
            }

            override fun afterTextChanged(p0: Editable?) {
                // do nothing
            }
        })
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                // do nothing
            }
        })
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                // do nothing
            }
        })

        binding.btnRegister.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
//            binding.emailEditTextLayout.isErrorEnabled = true
            when{
                username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() -> {
                    viewModel.init(username, email, password)
                    viewModel.getRegisterResult.observe(this) {
                        if (it != null){
                            if (it.message.equals("User created")) {
                                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }else
                                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                        }else
                            Toast.makeText(this, "Gagal terhubung ke jaringan", Toast.LENGTH_SHORT).show()
                    }
                }

             /*   username.isEmpty() -> binding.usernameEditTextLayout.error = "Field tidak boleh kosong"
                username.isNotEmpty() -> binding.usernameEditTextLayout.isErrorEnabled = false
                email.isEmpty() -> binding.emailEditTextLayout.error = getString(R.string.hint_email)
                email.isNotEmpty() -> binding.emailEditTextLayout.isErrorEnabled = false
                password.isEmpty() -> binding.passwordEditTextLayout.error = getString(R.string.hint_password)
                password.isNotEmpty() -> binding.passwordEditTextLayout.isErrorEnabled = false*/
            }

            viewModel.getLoading.observe(this) { showLoading(it) }
        }

        binding.layLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        playAnimation()
        setupView()
    }

    private fun setMyButton(){
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPassword.length()
        val username = binding.edtUsername.text.toString()
        binding.btnRegister.isEnabled = username.isNotEmpty() &&Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass >= 6
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(loading: Boolean) = if (loading)
        binding.progressbar.visibility = View.VISIBLE
    else
        binding.progressbar.visibility = View.GONE

    // animation
    @SuppressLint("Recycle")
    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val titleDesc = ObjectAnimator.ofFloat(binding.tvDescTitle, View.ALPHA, 1f).setDuration(500)

        val username = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(1000)
        val edtUsername = ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(1000)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(1000)
        val edtEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(1000)
        val edtPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(1000)

        val btnLogin = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val backToLogin = ObjectAnimator.ofFloat(binding.layLogin, View.ALPHA, 1f).setDuration(500)


        val together = AnimatorSet().apply {
            playTogether(username, edtUsername, email,edtEmail, password, edtPassword)
        }
        val together2 = AnimatorSet().apply {
            playTogether(title, titleDesc)
        }

        AnimatorSet().apply {
            playSequentially(together2, together, btnLogin, backToLogin)
            startDelay = 500
        }.start()
    }
}