package com.example.pokedex.data.responses

import java.io.Serializable

data class GenerationIv(
    val diamond_pearl: DiamondPearl,
    val heartgold_soulsilver: HeartgoldSoulsilver,
    val platinum: Platinum
) : Serializable