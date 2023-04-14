package com.example.pokedex.data.responses

import java.io.Serializable

data class UltraSunUltraMoon(
    val front_default: String,
    val front_female: Any,
    val front_shiny: String,
    val front_shiny_female: Any
) : Serializable