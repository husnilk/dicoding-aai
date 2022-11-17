package net.husnilkamil.dicodingstory.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import net.husnilkamil.dicodingstory.ui.register.RegisterActivity
import net.husnilkamil.dicodingstory.databinding.ActivityLoginBinding
import net.husnilkamil.dicodingstory.datamodels.LoginResponse
import net.husnilkamil.dicodingstory.helpers.Constant
import net.husnilkamil.dicodingstory.networks.NetworkConfig
import net.husnilkamil.dicodingstory.ui.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.textRegister.setOnClickListener{
            val registerIntent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        binding!!.buttonLogin.setOnClickListener {
            binding.progressLogin.visibility = View.VISIBLE
            val email = binding!!.edLoginEmail.text.toString()
            val password = binding!!.edLoginPassword.text.toString()
            userAuthCheck(email, password)
        }

        binding.progressLogin.visibility = View.GONE

        playAnimation()
    }

    private fun userAuthCheck(email: String, password: String)
    {
        val service = NetworkConfig.service
        val response = service.loginUser(email, password)
        response.enqueue(object : Callback<LoginResponse?> {

            override fun onResponse(call: Call<LoginResponse?>,response: Response<LoginResponse?>) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    val loginResult = loginResponse.loginResult
                    if (loginResult != null) {
                        val preferences =
                            getSharedPreferences(Constant.PREF_KEY_FILE_NAME, MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString(Constant.PREF_KEY_TOKEN, loginResult.token)
                        editor.putString(Constant.PREF_KEY_NAME, loginResult.name)
                        editor.putString(Constant.PREF_KEY_USERID, loginResult.userId)
                        editor.apply()

                        binding.progressLogin.visibility = View.GONE

                        loadMainActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, "Gagal Login", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Response Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                binding.progressLogin.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Terjadi kendala teknis", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loadMainActivity() {
        val homeActivity = Intent(this, HomeActivity::class.java)
        startActivity(homeActivity)
        finish()
    }

    fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding!!.imgLogo, View.ALPHA, 1f)
        logo.duration = 500
        val username = ObjectAnimator.ofFloat(binding!!.edLoginEmail, View.ALPHA, 1f)
        username.duration = 500
        val password = ObjectAnimator.ofFloat(binding!!.edLoginPassword, View.ALPHA, 1f)
        password.duration = 500
        val login = ObjectAnimator.ofFloat(binding!!.buttonLogin, View.ALPHA, 1f)
        login.duration = 500
        val register = ObjectAnimator.ofFloat(binding!!.textRegister, View.ALPHA, 1f)
        register.duration = 500
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(username, password, login, register)
        animatorSet.start()
    }
}