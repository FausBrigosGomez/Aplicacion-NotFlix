package com.example.aplicacionandroid.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.aplicacionandroid.R
import com.example.aplicacionandroid.databinding.ActivityDetallesPeliculaBinding
import com.example.aplicacionandroid.databinding.ActivityRegistroBinding
import com.example.aplicacionandroid.entidades.Pelicula
import com.example.aplicacionandroid.retrofit.ApiService
import com.example.aplicacionandroid.retrofit.Token
import com.example.aplicacionandroid.retrofit.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetallesPeliculaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallesPeliculaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_pelicula)

        binding = ActivityDetallesPeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val context = this
        val prefs = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE)

        val extras = intent.extras
        val id = extras?.getString("id")
        val token : String? = prefs.getString("token", "")


        val retrofit = Retrofit.Builder().baseUrl("https://damapi.herokuapp.com/api/v1/").addConverterFactory(
            GsonConverterFactory.create()).client(
            OkHttpClient()
        ).build()
        val api_service = retrofit.create(ApiService::class.java)


        //intent que dirige a ListActivity
        val intentList = Intent(context, ListActivity::class.java)




        /**
            CODIGO BOTÓN DE ACTUALIZAR/GUARDAR ↓
         */
        binding.btGuardar.setOnClickListener{

            //los datos de los edit text
            val titulo = binding.titulo.text.toString()
            val director = binding.genero.text.toString()
            val genero = binding.genero.text.toString()
            val sinopsis = binding.sinopsis.text.toString()
            val nota = binding.nota.text.toString()
            // val caratula = binding.ivCaratula.   ??

            //COMPROBAR SI HAY ALGUNO NULO -> mensaje de error si pasa

            //meter id? ↓
            if(titulo == null || director == null || genero == null || sinopsis == null || nota == null){
                Toast.makeText(context, "Alguno de los campos de la película son nulos", Toast.LENGTH_LONG).show()
            }
            else{
                //meter aquí el resto de código????
            }




            //guardar los datos de las películas // añadir la película al array

            if(id != null){
                //id distinto de nulo -> actualizar la peícula

                //película con los datos actualizados
                val peliActu = Pelicula(id, titulo, director, genero, nota, sinopsis, null)

                val updatePeliculaCall = api_service.update(peliActu, "Bearer " + token)
                updatePeliculaCall.enqueue(object: Callback<Pelicula>{
                    override fun onResponse(call: Call<Pelicula>, response: Response<Pelicula>
                    ) {
                        if(response.code() >= 200 && response.code() < 300){
                            //se actualiza la película


                            /**
                             * ERRORES:
                             * me actualiza bien el titulo pero no el nombre del director (sin dar error)
                             * en el resto de películas me da error 400
                             *
                             *
                              */
                            Toast.makeText(context, "La película fue actualizada.", Toast.LENGTH_SHORT).show()
                            startActivity(intentList)
                        }
                        else{
                            //error al actualizar la película

                            //ERROR 400
                            Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                            Toast.makeText(context, "No se pudo actualizar la película", Toast.LENGTH_SHORT).show()
                        }



                    }

                    override fun onFailure(call: Call<Pelicula>, t: Throwable) {
                        //error en la llamada a retrofit
                        Toast.makeText(context, "Error al intentar mostrar la película", Toast.LENGTH_SHORT).show()
                    }
                })


            }
            else{
                //el id es null -> se crea de cero la película

                //película con los datos nuevos
                val peliNueva = Pelicula(null,titulo, director, genero, nota, sinopsis, "")

                val crearPeliculaCall = api_service.create(peliNueva, "Bearer " + token)
                crearPeliculaCall.enqueue(object: Callback<Pelicula>{
                    override fun onResponse(call: Call<Pelicula>, response: Response<Pelicula>
                    ) {
                        if(response.code() >= 200 && response.code() < 300){
                            //se crea la película
                            Toast.makeText(context, "La película fue creada", Toast.LENGTH_SHORT).show()
                            startActivity(intentList)
                        }
                        else{
                            //error al actualizar la película
                            Toast.makeText(context, "Error al crear la película", Toast.LENGTH_SHORT).show()
                        }


                    }

                    override fun onFailure(call: Call<Pelicula>, t: Throwable) {
                        //error en la llamada de retrofit
                        Toast.makeText(context, "Error ?????", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        /**
            CODIGO BOTÓN BORRAR ↓
         */
        binding.btBorrar.setOnClickListener{
            val borrarCall = api_service.delete(id, "Bearer " + token)
            borrarCall.enqueue(object: Callback<Pelicula>{
                override fun onResponse(call: Call<Pelicula>, response: Response<Pelicula>) {

                }

                override fun onFailure(call: Call<Pelicula>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })




        }




        /**
            MOSTAR PELI CON RETROFIT ↓
         */
        val peliculaCall = api_service.getbyid(id, "Bearer " + token)
        peliculaCall.enqueue(object: Callback<Pelicula> {
            override fun onResponse(call: Call<Pelicula>, response: Response<Pelicula>) {
                if(response.code()>=200 && response.code()<299){
                    //todo bien - metemos los datos de la pelicula
                    val p = response.body()

                    binding.titulo.setText(p?.titulo)
                    binding.director.setText(p?.director)
                    binding.genero.setText(p?.genero)
                    binding.sinopsis.setText(p?.sinapsis)
                    binding.nota.setText(p?.nota)
                }
                else{
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, "Error al abrir la película", Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<Pelicula>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })





    }
}