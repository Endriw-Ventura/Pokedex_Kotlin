package com.example.pokedex.repository

import com.example.pokedex.data.remote.PokeApi
import com.example.pokedex.data.responses.PokemonData
import com.example.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<MutableList<PokemonData>> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error(mutableListOf(),"Error occurred while fetching Pokemon list.")
        }

        val pokemonListWithColor: MutableList<PokemonData> = mutableListOf()

        for (data in response.results) {
            val pokemonResult = getPokemonInfo(data.name)
            if (pokemonResult is Resource.Success) {
                val pokemonData = pokemonResult.data
                val pokemon = pokemonData?.let {
                    PokemonData(
                        it.abilities,
                        pokemonData.base_experience,
                        pokemonData.forms,
                        pokemonData.game_indices,
                        pokemonData.height,
                        pokemonData.held_items,
                        pokemonData.id,
                        pokemonData.is_default,
                        pokemonData.location_area_encounters,
                        pokemonData.moves,
                        pokemonData.name,
                        pokemonData.order,
                        pokemonData.past_types,
                        pokemonData.species,
                        pokemonData.sprites,
                        pokemonData.stats,
                        pokemonData.types,
                        pokemonData.weight
                    )
                }
                pokemonListWithColor.add(pokemon!!)
            } else {
                return Resource.Error(mutableListOf(),"Error occurred while fetching Pokemon ${data.name}.")
            }
        }
        return Resource.Success(pokemonListWithColor)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<PokemonData> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        }catch(e: Exception) {
            return Resource.Error(message = "Error Occurred")
        }
        return Resource.Success(response)
    }
}