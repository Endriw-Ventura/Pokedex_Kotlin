package com.example.pokedex.data.responses

import java.io.Serializable

data class VersionGroup(
    val name: String,
    val url: String
) : Serializable