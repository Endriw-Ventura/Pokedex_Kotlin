package com.example.pokedex.util

import com.example.pokedex.consts.consts
import java.util.*

object PokemonUtil {
     fun turnInCamelCase(word: String) =
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }

     fun getPokemonUrl(url: String): String {
        return consts.IMG_URL.replace("idPokemon", url)
    }
}