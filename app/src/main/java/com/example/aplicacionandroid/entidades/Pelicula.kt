package com.example.aplicacionandroid.entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pelicula (
    var id: String?,
    @SerializedName("title")
    var titulo: String?,
    @SerializedName("directorFullname")
    var director: String?,
    @SerializedName("genre")
    val genero: String?,
    @SerializedName("rating")
    var nota: String?,
    @SerializedName("description")
    var sinapsis: String?,
    @SerializedName("imageUrl")
    var caratula: String?
): Serializable