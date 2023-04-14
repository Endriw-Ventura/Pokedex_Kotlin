package com.example.pokedex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.di.modules.AppModule.providePokeApi
import com.example.pokedex.di.modules.AppModule.providePokemonRepository
import com.example.pokedex.repository.PokemonRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {
    private var page = 0
    private val PAGE_SIZE = 20
    private var job: Job = Job()
    private lateinit var binding: ActivityMainBinding
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateinit var adapter: PokemonAdapter
        val pokeApi = providePokeApi()
        val repository = providePokemonRepository(pokeApi)
        val recyclerView = binding.rvPokemonsList

        pokemonAdapterSetup(repository, recyclerView)

    }

    private fun pokemonAdapterSetup(
        repository: PokemonRepository,
        recyclerView: RecyclerView
    ) {
        binding.pbPokemonList.visibility = View.VISIBLE // Mostra o ProgressBar
        launch(coroutineContext) {
            val pokemonsList = repository.getPokemonList(PAGE_SIZE, page * PAGE_SIZE)
            val adapter = PokemonAdapter(pokemonsList)
            recyclerView.adapter = adapter
            binding.pbPokemonList.visibility = View.GONE // Esconde o ProgressBar

            configureOnScrollListener(recyclerView, repository, adapter)

        }
    }

    private fun configureOnScrollListener(
        recyclerView: RecyclerView,
        repository: PokemonRepository,
        adapter: PokemonAdapter
    ) {
        recyclerView.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1) && dy > 0) {
                        launch {
                            binding.pbPokemonList.visibility = View.VISIBLE // Mostra o ProgressBar

                            page++ //aumenta o número de páginas
                            val pokemons = repository.getPokemonList(PAGE_SIZE, page * PAGE_SIZE)
                            adapter.updateData(pokemons.data!!)

                            binding.pbPokemonList.visibility = View.GONE // Esconde o ProgressBar
                        }
                    } else {
                        super.onScrolled(recyclerView, dx, dy)
                    }
                }
            })
        }
    }
}