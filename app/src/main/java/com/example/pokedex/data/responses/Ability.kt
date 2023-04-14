package com.example.pokedex.data.responses

import java.io.Serializable

data class Ability(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
) : Serializable