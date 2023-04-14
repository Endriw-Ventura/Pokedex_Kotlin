package com.example.pokedex.data.responses

import java.io.Serializable

data class GenerationI(
    val red_blue: RedBlue,
    val yellow: Yellow
) : Serializable