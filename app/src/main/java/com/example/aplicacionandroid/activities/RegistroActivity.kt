package com.example.aplicacionandroid.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.aplicacionandroid.R
import com.example.aplicacionandroid.databinding.ActivityRegistroBinding
import com.example.aplicacionandroid.retrofit.ApiService
import com.example.aplicacionandroid.retrofit.Token
import com.example.aplicacionandroid.retrofit.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val context = this

        /**
            CODIGO PARA REGISTRARSE ↓
         */
        binding.btRegistrarse.setOnClickListener{
            val user = binding.tvCorreo.text.toString()
            val pass = binding.tvContrasenha.text.toString()

            val retrofit = Retrofit.Builder().baseUrl("https://damapi.herokuapp.com/api/v1/").addConverterFactory(
                GsonConverterFactory.create()).client(
                OkHttpClient()
            ).build()
            val api_service = retrofit.create(ApiService::class.java)
            val u = User(user, pass)


            val userCall = api_service.signup(u)
            userCall.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Log.d("login", "onResponse")
                    if(response.code()>=200 && response.code()<299){
                        //todo bien
                        Toast.makeText(applicationContext, "¡Usuario creado correctamente!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)

                    }
                    else{
                        //error
                        Toast.makeText(applicationContext, "Error al crear el usuario", Toast.LENGTH_SHORT).show()

                    }


                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }

    }
}