package com.example.pokedex.data.responses

import java.io.Serializable

data class GameIndice(
    val game_index: Int,
    val version: Version
) : Serializable