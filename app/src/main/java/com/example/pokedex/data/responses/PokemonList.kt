package com.example.pokedex.data.responses

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: ArrayList<Result>
)