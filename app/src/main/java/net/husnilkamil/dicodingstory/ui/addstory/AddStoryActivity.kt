package net.husnilkamil.dicodingstory.ui.addstory

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import net.husnilkamil.dicodingstory.databinding.ActivityAddStoryBinding
import net.husnilkamil.dicodingstory.data.networks.Response.ObjectResponse
import net.husnilkamil.dicodingstory.utils.createCustomTempFile
import net.husnilkamil.dicodingstory.utils.getToken
import net.husnilkamil.dicodingstory.utils.uriToFile
import net.husnilkamil.dicodingstory.data.networks.NetworkConfig
import net.husnilkamil.dicodingstory.data.networks.request.StoryRequest
import net.husnilkamil.dicodingstory.ui.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentPhotoPath: String? = null
    private var uploadFile: File? = null
    private lateinit var addStoryViewModel: AddStoryViewModel

    var launcherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImage = result.data!!.data
                binding!!.ivPhoto.setImageURI(selectedImage)
                try {
                    val myFile = selectedImage?.let { uriToFile(it, this@AddStoryActivity) }
                    uploadFile = myFile
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    var launcherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val myFile = File(currentPhotoPath)
                uploadFile = myFile
                val imageBitmap = BitmapFactory.decodeFile(myFile.path)
                binding!!.ivPhoto.setImageBitmap(imageBitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        binding!!.progressUpload.visibility = View.GONE
        binding!!.buttonAdd.setOnClickListener { uploadStory() }
        binding!!.buttonCamera.setOnClickListener { takePicture() }
        binding!!.buttonGalery.setOnClickListener { pickImage() }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherGallery.launch(chooser)
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(this.packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "net.husnilkamil.dicodingstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherCamera.launch(intent)
        }

    }

    private fun uploadStory() {

        if (uploadFile != null) {
            val description = binding!!.edAddDescription.text.toString()

            val desc = description.toRequestBody("text/plain".toMediaType())
            val img = uploadFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", uploadFile!!.name, img)

            val storyRequest = StoryRequest(getToken(this@AddStoryActivity), desc, imageMultiPart)

            val factory = ViewModelFactory.getInstance(this)
            addStoryViewModel = ViewModelProvider(this, factory).get(AddStoryViewModel::class.java)
            binding.progressUpload.visibility = View.VISIBLE
            addStoryViewModel.uploadStory(storyRequest).observe(this) {
                binding.progressUpload.visibility = View.GONE
                if(it.error == true){
                    Toast.makeText(this, "Oops terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Berhasil tambah story", Toast.LENGTH_SHORT).show()
                }
            }
            
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                "Silahkan tambahkan/pilih photo terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}