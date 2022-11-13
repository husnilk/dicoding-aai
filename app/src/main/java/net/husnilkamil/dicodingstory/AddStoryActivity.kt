package net.husnilkamil.dicodingstory

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import net.husnilkamil.dicodingstory.databinding.ActivityAddStoryBinding
import net.husnilkamil.dicodingstory.datamodels.ObjectResponse
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
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.File
import java.io.IOException

class AddStoryActivity : AppCompatActivity() {

    private var binding: ActivityAddStoryBinding? = null
    private var currentPhotoPath: String? = null
    private var uploadFile: File? = null

    var launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
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
    var launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
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
        var myFile: File? = null
        try {
            myFile = net.husnilkamil.dicodingstory.helpers.createTempFile(this)
            val photoUri = FileProvider.getUriForFile(
                this,
                "net.husnilkamil.ceritakita",
                myFile
            )
            currentPhotoPath = myFile.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherCamera.launch(intent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun uploadStory() {
        if (uploadFile != null) {
            val description = binding!!.edAddDescription.toString()

            val desc = description.toRequestBody("text/plain".toMediaType())
            val img = uploadFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData("file", uploadFile!!.name, img)

            val service = NetworkConfig.service
            val response = service.addStories(getToken(this), imageMultiPart, desc)
            response.enqueue(object : Callback<ObjectResponse?> {
                override fun onResponse(
                    call: Call<ObjectResponse?>,
                    response: Response<ObjectResponse?>
                ) {
                    val objectResponse = response.body()
                    if (objectResponse != null) {
                        if (!objectResponse.error!!) {
                            Toast.makeText(
                                this@AddStoryActivity,
                                "Story berhasil diupload",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@AddStoryActivity,
                                "Gagal mengupload story",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@AddStoryActivity, "Wah kenapa ini?", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ObjectResponse?>, t: Throwable) {
                    Log.d("DBG-ADD", t.message!!)
                }
            })
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                "Silahkan masukkan berkas gambar terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}