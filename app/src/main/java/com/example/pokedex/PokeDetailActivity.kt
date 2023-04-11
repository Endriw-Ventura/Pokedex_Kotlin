package com.example.pokedex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.example.pokedex.consts.consts
import com.example.pokedex.di.modules.AppModule
import com.example.pokedex.databinding.ActivityPokeDetailBinding
import com.example.pokedex.util.PokemonTypes
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
                tvPokemonName.text = pokemon.data!!.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
                tvPokemonId.text = "#${pokemon.data.id}"

                val colorTypeName = pokemon.data.types.first().type.name
                val colorTypeInt = PokemonTypes.getTypeColor(colorTypeName)

                tvPokemonType1.text = colorTypeName.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }

                tvPokemonType1.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, colorTypeInt)
                //tvPokemonType1.setTextColor(ContextCompat.getColorStateList(binding.root.context, colorTypeInt))
                flPokemonImage.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, colorTypeInt)

                if (colorTypeName != pokemon.data!!.types.last().type.name){
                    val colorType2Name = pokemon.data!!.types.last().type.name
                    val colorType2Int = PokemonTypes.getTypeColor(colorType2Name)
                    tvPokemonType2.text = colorType2Name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }
                    tvPokemonType2.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, colorType2Int)
                    //tvPokemonType2.setTextColor(ContextCompat.getColorStateList(binding.root.context, colorType2Int))
                }else{
                    tvPokemonType2.visibility = View.GONE
                }

                pbPokemonDetails.visibility = View.VISIBLE
                ivPokemonImageDetails.load(consts.IMG_URL.replace("idPokemon", pokemon.data.id.toString()))
                {
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


                tvPokemonHeight.text = pokemon.data.height.toString()
                tvPokemonWeight.text = pokemon.data.weight.toString()

                pokemon.data.stats.apply {
                    tvPokemonHp.text = this.find { it.stat.name == "hp" }!!.base_stat.toString()
                    tvPokemonAtk.text = this.find { it.stat.name == "attack" }!!.base_stat.toString()
                    tvPokemonDef.text = this.find { it.stat.name == "defense" }!!.base_stat.toString()
                    tvPokemonSpeed.text = this.find{it.stat.name == "speed"}!!.base_stat.toString()
                }
            }
        }
    }
}