package net.husnilkamil.dicodingstory.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import net.husnilkamil.dicodingstory.databinding.ActivityRegisterBinding
import net.husnilkamil.dicodingstory.data.networks.Response.ObjectResponse
import net.husnilkamil.dicodingstory.data.networks.NetworkConfig
import net.husnilkamil.dicodingstory.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.buttonRegister.setOnClickListener {
            val username = binding!!.edRegisterName.text.toString()
            val password = binding!!.edRegisterPassword.text.toString()
            val email = binding!!.edRegisterEmail.text.toString()
            binding!!.progressRegistrasi.visibility = View.VISIBLE
            registerUser(username, email, password)
        }

        binding!!.textLogin.setOnClickListener {
            val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        binding!!.progressRegistrasi.visibility = View.GONE
    }

    private fun registerUser(username: String, email: String, password: String) {

        val service = NetworkConfig.service

        val response = service.registerUser(username, email, password)
        response.enqueue(object : Callback<ObjectResponse?> {

            override fun onResponse(call: Call<ObjectResponse?>, response: Response<ObjectResponse?>)
            {
                val registrationResponse = response.body()

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
                binding!!.progressRegistrasi.visibility = View.GONE
            }

            override fun onFailure(call: Call<ObjectResponse?>, t: Throwable)
            {
                Toast.makeText(
                    this@RegisterActivity,
                    "Terjadi kendala teknis. Registrasi gagal",
                    Toast.LENGTH_SHORT
                ).show()
                binding!!.progressRegistrasi.visibility = View.GONE
            }
        })

    }
}