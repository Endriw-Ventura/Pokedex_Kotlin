package com.example.pokedex.data.responses

import java.io.Serializable

data class Yellow(
    val back_default: String,
    val back_gray: String,
    val back_transparent: String,
    val front_default: String,
    val front_gray: String,
    val front_transparent: String
) : Serializable