package com.ramaqq.storyapp_submission1.ui.detail

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ramaqq.storyapp_submission1.R
import com.ramaqq.storyapp_submission1.databinding.ActivityDetailStoryBinding
import com.ramaqq.storyapp_submission1.data.response.Story
import com.ramaqq.storyapp_submission1.data.local.entity.UserPreference
import com.ramaqq.storyapp_submission1.ui.getTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference.getInstance(dataStore)
        val viewModel: DetailStoryViewModel by viewModels()
        val id = intent.getStringExtra(ID_STORY).toString()
        viewModel.init(pref, id)

        viewModel.getDetailStory.observe(this) {
            if (it != null) setData(it)
        }
        viewModel.getLoading.observe(this){
            if (it) binding.progressbar.visibility = View.VISIBLE
            else binding.progressbar.visibility = View.GONE
        }
        viewModel.getError.observe(this@DetailStoryActivity){
            if (it) Toast.makeText(this@DetailStoryActivity, R.string.failed_load_page, Toast.LENGTH_SHORT).show()
        }

        binding.imbBack.setOnClickListener {
            finish()
        }
        setupView()

    }

    private fun setData(data: Story) {
        Glide.with(this)
            .load(data.photoUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_round_image_24)
            )
            .into(binding.imgStories)

        binding.tvTitle.text = data.description
        binding.tvUploader.text = this.getString(R.string.desc_upload, data.name)


        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        try {
            if (data.createdAt != null) {
                val date1 = input.parse(data.createdAt) // dipake
                val outputDate1 = getTime(date1, outputDate)
                binding.tvDate.text = getString(R.string.posted_on, outputDate1)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
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

    companion object {
        const val ID_STORY = "EXTRA_DATA"
    }
}