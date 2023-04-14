package com.example.pokedex.data.responses

import java.io.Serializable

data class Type(
    val slot: Int,
    val type: TypeX
) : Serializable