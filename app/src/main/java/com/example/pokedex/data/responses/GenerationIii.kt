package com.example.pokedex.data.responses

import java.io.Serializable

data class GenerationIii(
    val emerald: Emerald,
    val firered_leafgreen: FireredLeafgreen,
    val ruby_sapphire: RubySapphire
) : Serializable