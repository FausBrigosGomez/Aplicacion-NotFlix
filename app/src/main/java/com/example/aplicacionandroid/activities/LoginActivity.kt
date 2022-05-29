package com.example.aplicacionandroid.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.aplicacionandroid.databinding.ActivityLoginBinding
import com.example.aplicacionandroid.retrofit.ApiService
import com.example.aplicacionandroid.retrofit.Token
import com.example.aplicacionandroid.retrofit.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        context = this
        val prefs = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE)

        // TODO: Eliminar
        binding.correo.setText("prueba@gmail.com")
        binding.contrasenha.setText("1234")


        /**
            CODIGO PARA ENTRAR EN LA PANTALLA DE ListActivity ↓
         */
        binding.btLogin.setOnClickListener(){
            val correo = binding.correo.text.toString()
            val contrasenha = binding.contrasenha.text.toString()
            val u = User(correo, contrasenha)

            val retrofit = Retrofit.Builder().baseUrl("https://damapi.herokuapp.com/api/v1/").addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient()).build()
            val api_service = retrofit.create(ApiService::class.java)

            val tokenCall = api_service.login(u)
            tokenCall.enqueue(object: Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    Log.d("login", "onResponse")
                    if(response.code()>=200 && response.code()<299){
                        //todo bien
                        val token = response.body()?.token
                        val editor = prefs.edit()
                        editor.putString("token", token)
                        editor.apply()
                        val intent = Intent(context, ListActivity::class.java)
                        startActivity(intent)

                    }
                    else{
                        //error al crear el usuario
                        Toast.makeText(applicationContext, "Error al verificar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error ?????", Toast.LENGTH_SHORT).show()
                }
            })
        }

        /**
            CODIGO PARA ENTRAR EN LA PANTALLA DE REGISTRO
         */
        binding.btRegistro.setOnClickListener{
            val intent = Intent(context, RegistroActivity::class.java)
            startActivity(intent)
        }


    }
}