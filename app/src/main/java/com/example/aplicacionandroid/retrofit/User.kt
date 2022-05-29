package com.example.aplicacionandroid.retrofit

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("email")
    val correo: String,

    @SerializedName("password")
    val contrasenha: String) {
}