package com.example.pokedex.data.responses

import java.io.Serializable

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: ArrayList<Result>
) : Serializable