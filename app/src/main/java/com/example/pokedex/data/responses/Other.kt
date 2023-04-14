package com.example.pokedex.data.responses

import java.io.Serializable

data class Other(
    val dream_world: DreamWorld,
    val home: Home,
    val official_artwork: OfficialArtwork
) : Serializable