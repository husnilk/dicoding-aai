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
import net.husnilkamil.dicodingstory.databinding.ActivityAddStoryBinding
import net.husnilkamil.dicodingstory.models.ObjectResponse
import net.husnilkamil.dicodingstory.helpers.createCustomTempFile
import net.husnilkamil.dicodingstory.helpers.getToken
import net.husnilkamil.dicodingstory.helpers.uriToFile
import net.husnilkamil.dicodingstory.networks.NetworkConfig
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

    private var binding: ActivityAddStoryBinding? = null
    private var currentPhotoPath: String? = null
    private var uploadFile: File? = null

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

        binding!!.progressUpload.visibility = View.VISIBLE
        if (uploadFile != null) {
            val description = binding!!.edAddDescription.text.toString()

            val desc = description.toRequestBody("text/plain".toMediaType())
            val img = uploadFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", uploadFile!!.name, img)

            val service = NetworkConfig.service
            val response = service.addStories(getToken(this), imageMultiPart, desc)
            response.enqueue(object : Callback<ObjectResponse?> {

                override fun onResponse(call: Call<ObjectResponse?>,response: Response<ObjectResponse?>) {
                    val objectResponse = response.body()
                    Log.d("ADD-DBG", response.toString())
                    if (objectResponse != null && !objectResponse.error!!) {
                        if (!objectResponse.error) {
                            Toast.makeText(
                                this@AddStoryActivity,
                                objectResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddStoryActivity,
                                objectResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@AddStoryActivity,
                            "Gagal Upload Story",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding!!.progressUpload.visibility = View.GONE
                }

                override fun onFailure(call: Call<ObjectResponse?>, t: Throwable) {
                    Log.d("ADD-DBG", t.message.toString())
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Terjadi kesalahan teknis",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding!!.progressUpload.visibility = View.GONE
                }
            })
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                "Silahkan tambahkan/pilih photo terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}