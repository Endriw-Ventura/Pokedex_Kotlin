package com.example.pokedex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.example.pokedex.consts.consts
import com.example.pokedex.data.responses.PokemonData
import com.example.pokedex.di.modules.AppModule
import com.example.pokedex.databinding.ActivityPokeDetailBinding
import com.example.pokedex.util.PokemonTypes
import com.example.pokedex.util.Resource
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext


class PokeDetailActivity : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()
    private lateinit var binding: ActivityPokeDetailBinding
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokeApi = AppModule.providePokeApi()
        val repository = AppModule.providePokemonRepository(pokeApi)
        val pokemon_name = intent.getStringExtra("POKEMON_NAME")

        launch(coroutineContext) {
            val pokemon = repository.getPokemonInfo(pokemon_name!!)
            binding.apply {
                setPokemonInfo(pokemon)
                loadPokemonImage(pokemon.data!!.id)
            }
        }
    }

    private fun ActivityPokeDetailBinding.setPokemonInfo(pokemon: Resource<PokemonData>) {
        pokemon.data?.apply {
            val colorTypeName = types.first().type.name
            val colorTypeInt = PokemonTypes.getTypeColor(colorTypeName)

            tvPokemonName.text = turnInCamelCase(name)
            tvPokemonId.text = "#${id}"
            tvPokemonType1.text = turnInCamelCase(colorTypeName)

            tvPokemonType1.backgroundTintList =
                ContextCompat.getColorStateList(root.context, colorTypeInt)
            //tvPokemonType1.setTextColor(ContextCompat.getColorStateList(binding.root.context, colorTypeInt))
            flPokemonImage.backgroundTintList =
                ContextCompat.getColorStateList(root.context, colorTypeInt)

            setSecondPokemonType(colorTypeName, pokemon)
            pbPokemonDetails.visibility = View.VISIBLE
            tvPokemonHeight.text = height.toString()
            tvPokemonWeight.text = weight.toString()

            stats.apply {
                tvPokemonHp.text = this.find { it.stat.name == "hp" }!!.base_stat.toString()
                tvPokemonAtk.text =
                    this.find { it.stat.name == "attack" }!!.base_stat.toString()
                tvPokemonDef.text =
                    this.find { it.stat.name == "defense" }!!.base_stat.toString()
                tvPokemonSpeed.text =
                    this.find { it.stat.name == "speed" }!!.base_stat.toString()
            }
        }
    }

    private fun ActivityPokeDetailBinding.loadPokemonImage(id: Int) {
        ivPokemonImageDetails.load(getPokemonUrl(id.toString())) {
            //Listener para esconder o ProgressBar quando a imagem é carregada com sucesso
            listener(
                onSuccess = { request, metadata ->
                    pbPokemonDetails.visibility = View.GONE
                    ivPokemonImageDetails.visibility = View.VISIBLE
                },
                onError = { request, throwable ->
                    pbPokemonDetails.visibility = View.GONE
                    ivPokemonImageDetails.visibility = View.VISIBLE
                }
            )
        }
    }

    private fun ActivityPokeDetailBinding.setSecondPokemonType(
        colorTypeName: String,
        pokemon: Resource<PokemonData>
    ) {
        if (colorTypeName != pokemon.data!!.types.last().type.name) {
            val colorType2Name = pokemon.data!!.types.last().type.name
            val colorType2Int = PokemonTypes.getTypeColor(colorType2Name)

            tvPokemonType2.text = turnInCamelCase(colorType2Name)
            tvPokemonType2.backgroundTintList =
                ContextCompat.getColorStateList(binding.root.context, colorType2Int)
            //tvPokemonType2.setTextColor(ContextCompat.getColorStateList(binding.root.context, colorType2Int))
        } else {
            tvPokemonType2.visibility = View.GONE
        }
    }

    private fun turnInCamelCase(word: String) =
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }

    private fun getPokemonUrl(url: String): String {
        return consts.IMG_URL.replace("idPokemon", url)
    }
}