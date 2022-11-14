package net.husnilkamil.dicodingstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import net.husnilkamil.dicodingstory.databinding.ActivityRegisterBinding
import net.husnilkamil.dicodingstory.datamodels.ObjectResponse
import net.husnilkamil.dicodingstory.networks.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterDebug"
    var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.buttonRegister.setOnClickListener {
            val username = binding!!.edRegisterName.text.toString()
            val password = binding!!.edRegisterPassword.text.toString()
            val email = binding!!.edRegisterEmail.text.toString()
            registerUser(username, email, password)
        }

        binding!!.textLogin.setOnClickListener {
            val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    private fun registerUser(username: String, email: String, password: String) {

        val service = NetworkConfig.service

        val response = service.registerUser(username, email, password)
        response.enqueue(object : Callback<ObjectResponse?> {

            override fun onResponse(call: Call<ObjectResponse?>,response: Response<ObjectResponse?>)
            {
                Log.d(TAG, response.toString())
                Log.d(TAG, response.message())
                val registrationResponse = response.body()
                Log.d(TAG, registrationResponse.toString())

                if (registrationResponse != null && !registrationResponse.error!!) {
                    val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(loginIntent)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi Berhasil. Silahkan login",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi Gagal. " + registrationResponse!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ObjectResponse?>, t: Throwable)
            {
                Log.d(TAG, t.message!!)
                Toast.makeText(
                    this@RegisterActivity,
                    "Terjadi kendala teknis. Registrasi gagal",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}