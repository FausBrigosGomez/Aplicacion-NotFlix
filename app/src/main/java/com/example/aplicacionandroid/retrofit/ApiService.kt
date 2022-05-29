package com.example.aplicacionandroid.retrofit

import com.example.aplicacionandroid.entidades.Pelicula
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("users/login")
    fun login(@Body usuario:User): Call<Token>

    @GET("movies")
    fun getAll(@Header("Authorization") token: String):Call<List<Pelicula>>

    @POST("users/signup")
    fun signup(@Body users: User): Call<User>

    @GET("movies/{id}")
    fun getbyid(@Path(value = "id") id: String?, @Header("Authorization") token: String): Call<Pelicula>

    @POST("movies")
    fun create(@Body pelicula: Pelicula, @Header("Authorization") token: String): Call<Pelicula> // ?

    @PUT("movies")
    fun update(@Body pelicua: Pelicula, @Header("Authorization") token: String): Call<Pelicula> // ?

    @DELETE("movies/{id}")
    fun delete(@Path(value = "id") id: String?, @Header("Authorization") token: String): Call<Pelicula> // ?
}