package com.example.aplicacionandroid.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionandroid.R
import com.example.aplicacionandroid.adapters.ListAdapter
import com.example.aplicacionandroid.databinding.ActivityListBinding
import com.example.aplicacionandroid.entidades.Pelicula
import com.example.aplicacionandroid.retrofit.ApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val context = this
        val prefs = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE)
        val token : String? = prefs.getString("token", "")

        val retrofit = Retrofit.Builder().baseUrl("https://damapi.herokuapp.com/api/v1/").addConverterFactory(
            GsonConverterFactory.create()).client(OkHttpClient()).build()
        val api_service = retrofit.create(ApiService::class.java)
        Toast.makeText(context, "token: " + token, Toast.LENGTH_SHORT).show()


        /**
            CODIGO MOSTRAR TODAS LAS PELÍCULAS EN EL RECYCLER VIEW ↓
         */
        val listCall = api_service.getAll("Bearer " + token)
        listCall.enqueue(object: Callback<List<Pelicula>>{
            override fun onResponse(call: Call<List<Pelicula>>, response: Response<List<Pelicula>>) {
                //Toast.makeText(applicationContext, "Respuesta: ${response.code()} == 401", Toast.LENGTH_SHORT).show()
                if(response.code() >= 200 && response.code() < 299){
                    //todo bien
                    val lista = response.body()
                    val rvPelicuas = binding.rvPeliculas
                    val layoutManager = LinearLayoutManager(context)
                    val adapter = ListAdapter(lista, context)

                    rvPelicuas.adapter = adapter
                    rvPelicuas.layoutManager = layoutManager
                    //Toast.makeText(applicationContext, "token: " + token, Toast.LENGTH_SHORT).show()

                    //botón para añadir la película
                    binding.btAnadir.setOnClickListener{
                        val intent = Intent(context, DetallesPeliculaActivity::class.java)
                        startActivity(intent)
                    }


                } else if(response.code() == 401){
                    Toast.makeText(applicationContext, "Error al autentificar al usuario. Vuelve a identificarte.", Toast.LENGTH_SHORT).show()
                    // Como ha fallado la auatentiacion, reinicio la app desde el login
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

            }

            override fun onFailure(call: Call<List<Pelicula>>, t: Throwable) {
                //error al cargar la lista
                Toast.makeText(applicationContext, "Error en la conexion", Toast.LENGTH_SHORT).show()
                finishAndRemoveTask()
            }

        })






    }



}