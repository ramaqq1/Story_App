package com.ramaqq.storyapp_submission1.ui.addstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ramaqq.storyapp_submission1.R
import com.ramaqq.storyapp_submission1.databinding.ActivityAddStoryBinding
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import com.ramaqq.storyapp_submission1.ui.MainPageActivity
import com.ramaqq.storyapp_submission1.ui.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity(){
    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null
    private lateinit var pref: UserPreference
    private val viewModel: AddStoryViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionGranted())
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)

        pref = UserPreference.getInstance(dataStore)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGalery.setOnClickListener { startImageGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
        binding.imbLocation.setOnClickListener { getMyCurrentLocation() }
    }


    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionGranted()) {
                Toast.makeText(this@AddStoryActivity, "Tidak mendapat ijin menggunkan kamera", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startImageGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "chose a Picture")
        launcherGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = BitmapFactory.decodeFile(myFile.path)
            getFile = myFile

            /* change picture rotation
             val result = rotateBitmap(
                 BitmapFactory.decodeFile(myFile.path), isBackCamera
             )*/
            binding.imgPreview.setImageBitmap(result)
            if (getFile != null) binding.tvHint.visibility = View.GONE
            else binding.tvHint.visibility = View.VISIBLE
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri

            val myFile = uriToFile(selectedImage, this@AddStoryActivity)

            getFile = myFile
            binding.imgPreview.setImageURI(selectedImage)
            if (getFile != null) binding.tvHint.visibility = View.GONE
            else binding.tvHint.visibility = View.VISIBLE
        }
    }

    private fun uploadImage() {
        if (getFile != null && binding.edtDescrition.text!!.isNotEmpty()) {
            val file = reduceFile(getFile as File)
            val description = binding.edtDescrition.text.toString().trim()

            val requestImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestImage)

            viewModel.init(pref, description, imageMultipart)
            viewModel.getStatus.observe(this) {
                if (it != null) {
                    if (it.equals("Story created successfully")) {
                        Toast.makeText(this@AddStoryActivity, getString(R.string.succes_add_story), Toast.LENGTH_SHORT).show()
/*                        Intent(this@AddStoryActivity, MainPageActivity::class.java).also { result ->
                            startActivity(result)
                            finish()
                        }*/
                        finish()
                    } else
                        Toast.makeText(
                            this@AddStoryActivity,
                            getString(R.string.failed_add_story),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
            viewModel.getError.observe(this) {
                if (it) Toast.makeText(
                    this@AddStoryActivity,
                    getString(R.string.failed_add_story),
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.getLoading.observe(this) { loading(it) }
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                "Please enter your image and description...",
                Toast.LENGTH_SHORT
            ).show()

        }
    }


    private fun reduceFile(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path) // to bitmap
        var compressQuality = 100
        var streamLength: Int

        do {
            val bitmapStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bitmapStream)
            val bytePicArray = bitmapStream.toByteArray()
            streamLength = bytePicArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            compressQuality,
            FileOutputStream(file)
        ) // file?

        return file
    }

    private fun loading(status: Boolean) {
        if (status)
            binding.progressbar.visibility = View.VISIBLE
        else
            binding.progressbar.visibility = View.GONE
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){
        when{
            it[Manifest.permission.ACCESS_FINE_LOCATION] ?: false ->{
                getMyCurrentLocation()
            }
            it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false ->{
                getMyCurrentLocation()
            }
            else ->{}
        }
    }

    private fun checkPermission(permission: String): Boolean{
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyCurrentLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            // get location
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    val loc = "${it.latitude}, ${it.longitude}"
                    binding.edtLoc.setText(loc)
                    viewModel.setLocation(it)
                }else
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }else
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }



}