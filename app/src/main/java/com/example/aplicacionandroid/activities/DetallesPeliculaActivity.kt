package com.example.aplicacionandroid.activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.aplicacionandroid.R
import com.example.aplicacionandroid.databinding.ActivityDetallesPeliculaBinding
import com.example.aplicacionandroid.entidades.Pelicula
import com.example.aplicacionandroid.retrofit.ApiService
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

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
        val token: String? = prefs.getString("token", "")


        val retrofit =
            Retrofit.Builder().baseUrl("https://damapi.herokuapp.com/api/v1/").addConverterFactory(
                GsonConverterFactory.create()
            ).client(
                OkHttpClient()
            ).build()
        val api_service = retrofit.create(ApiService::class.java)





        /**
        CODIGO BOTÓN DE ACTUALIZAR/GUARDAR ↓
         */
        binding.btGuardar.setOnClickListener {

            //los datos de los edit text
            val titulo = binding.titulo.text.toString()
            val director = binding.director.text.toString()
            val genero = binding.genero.text.toString()
            val sinopsis = binding.sinopsis.text.toString()
            val nota = binding.nota.text.toString()
            val caratula = binding.ivCaratula.toString()

            //COMPROBAR SI HAY ALGUNO NULO -> mensaje de error si pasa

            //meter id? ↓
            if (titulo.isEmpty() || director.isEmpty() || genero.isEmpty() || sinopsis.isEmpty() || nota.isEmpty() || caratula.isEmpty()) {
                Toast.makeText(
                    context,
                    "Alguno de los campos de la película están vacíos",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //meter aquí el resto de código????
            }


            //guardar los datos de las películas // añadir la película al array

            if (id != null) {
                //id distinto de nulo -> actualizar la peícula

                //película con los datos actualizados
                val peliActu = Pelicula(id, titulo, director, genero, nota, sinopsis, null)

                val updatePeliculaCall = api_service.update(peliActu, "Bearer " + token)
                updatePeliculaCall.enqueue(object : Callback<Pelicula> {
                    override fun onResponse(
                        call: Call<Pelicula>, response: Response<Pelicula>
                    ) {
                        if (response.code() >= 200 && response.code() < 300) {
                            //se actualiza la película
                            Toast.makeText(
                                context,
                                "La película fue actualizada.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            //error al actualizar la película

                            //ERROR 400
                            Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                                .show()
                            Toast.makeText(
                                context,
                                "No se pudo actualizar la película",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }

                    override fun onFailure(call: Call<Pelicula>, t: Throwable) {
                        //error en la llamada a retrofit
                        Toast.makeText(
                            context,
                            "Error al intentar mostrar la película",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })


            } else {
                //el id es null -> se crea de cero la película

                //película con los datos nuevos
                val peliNueva = Pelicula(null, titulo, director, genero, nota, sinopsis, null)

                val crearPeliculaCall = api_service.create(peliNueva, "Bearer " + token)
                crearPeliculaCall.enqueue(object : Callback<Pelicula> {
                    override fun onResponse(
                        call: Call<Pelicula>, response: Response<Pelicula>
                    ) {
                        if (response.code() >= 200 && response.code() < 300) {
                            //se crea la película
                            Toast.makeText(context, "La película fue creada", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        } else {
                            //error al crear la película
                            Toast.makeText(
                                context,
                                "Error al crear la película",
                                Toast.LENGTH_SHORT
                            ).show()
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
        binding.btBorrar.setOnClickListener {
            val borrarCall = api_service.delete(id, "Bearer " + token)
            borrarCall.enqueue(object : Callback<Pelicula> {
                override fun onResponse(call: Call<Pelicula>, response: Response<Pelicula>) {
                    if (response.code() >= 200 && response.code() < 300) {
                        //se elimina la película
                        Toast.makeText(context, "La película fue eliminada", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        //error al borrar la película
                        Toast.makeText(
                            context,
                            "Error al borrar la película",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Pelicula>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


        }




        /**
        CAMBIAR EL TITULO SEGUN SI SE CREA O SE ACTUALIZA
         */
        if(id == null){
            this.setTitle("Nueva película")
            binding.btGuardar.text = "¡Crear película!"
            binding.btBorrar.visibility = View.GONE
        }
        else{
            binding.btGuardar.text = "Actualizar"
        }
        /**
        MOSTAR PELI CON RETROFIT ↓
         */
        if (id != null) {

            val peliculaCall = api_service.getbyid(id, "Bearer " + token)
            peliculaCall.enqueue(object : Callback<Pelicula> {
                override fun onResponse(call: Call<Pelicula>, response: Response<Pelicula>) {
                    if (response.code() >= 200 && response.code() < 299) {
                        //todo bien - metemos los datos de la pelicula
                        val p = response.body()

                        binding.titulo.setText(p?.titulo)
                        binding.director.setText(p?.director)
                        binding.genero.setText(p?.genero)
                        binding.sinopsis.setText(p?.sinapsis)
                        binding.nota.setText(p?.nota)

                        try{
                            Picasso.get().load(p?.caratula).into(binding.ivCaratula)
                        }catch(e: Exception){
                            Toast.makeText(applicationContext, "No se ha podido cargar la imagen", Toast.LENGTH_SHORT).show()
                        }

                        context.setTitle(p?.titulo)
                    } else {
                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                        Toast.makeText(context, "Error al abrir la película", Toast.LENGTH_SHORT)
                            .show()
                    }


                }

                override fun onFailure(call: Call<Pelicula>, t: Throwable) {
                    Toast.makeText(context, "Error on failure -> getbyid", Toast.LENGTH_SHORT)
                        .show()
                }
            })


        }
    }

    override fun onResume() {
        super.onResume()

        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}