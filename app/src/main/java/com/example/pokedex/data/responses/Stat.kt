package com.example.pokedex.data.responses

import java.io.Serializable

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatX
) : Serializable