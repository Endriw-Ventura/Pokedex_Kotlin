package com.example.pokedex.data.responses

import java.io.Serializable

data class OfficialArtwork(
    val front_default: String,
    val front_shiny: String
) : Serializable