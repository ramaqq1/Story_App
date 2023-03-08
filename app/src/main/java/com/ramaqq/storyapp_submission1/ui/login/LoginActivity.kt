package com.ramaqq.storyapp_submission1.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ramaqq.storyapp_submission1.databinding.ActivityLoginBinding
import com.ramaqq.storyapp_submission1.data.response.LoginResult
import com.ramaqq.storyapp_submission1.data.local.entity.UserPreference
import com.ramaqq.storyapp_submission1.ui.MainPageActivity
import com.ramaqq.storyapp_submission1.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = UserPreference.getInstance(dataStore)
        val viewModel: LoginViewModel by viewModels()

        setMyButton()
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

        binding.layRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString()

            when {
                email.isNotEmpty() && password.isNotEmpty()-> {
                    viewModel.init(email, password, preferences)
                    viewModel.getLoginResult.observe(this) {
                            if (it.message == "success" && it.loginResult != null) {
                                    viewModel.saveDataLogin(LoginResult(it.loginResult.name, it.loginResult.userId, it.loginResult.token), email)
                                    startActivity(Intent(this, MainPageActivity::class.java))
                                    finish()
                            } else
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            viewModel.getLoading.observe(this) {
                when {
                    it -> binding.progressbar.visibility = View.VISIBLE
                    else -> binding.progressbar.visibility = View.INVISIBLE
                }
            }
            viewModel.getError.observe(this){
                when {
                    it -> Toast.makeText(this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()

                }
            }

        }

        setupView()
        playAnimation()
    }

    private fun setMyButton(){
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPassword.length()
        binding.btnLogin.isEnabled = Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass >= 8
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

    override fun onClick(view: View?) {
        if (view != null) {
            if (view == binding.layRegister)
                startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // animation
    @SuppressLint("Recycle")
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.tvTitle, View.TRANSLATION_X,  -30f, 60f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)

        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val titleDesc = ObjectAnimator.ofFloat(binding.tvDescTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.layRegister, View.ALPHA, 1f).setDuration(500)


        val together = AnimatorSet().apply {
            playTogether(email,edtEmail, password, edtPassword)
        }
        val together2 = AnimatorSet().apply {
            playTogether(titleDesc, desc)
        }

        AnimatorSet().apply {
            playSequentially(image, together2, together, btnLogin, register)
            start()
        }
    }
}