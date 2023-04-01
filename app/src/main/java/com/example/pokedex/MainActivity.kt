package com.example.pokedex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.di.modules.AppModule.providePokeApi
import com.example.pokedex.di.modules.AppModule.providePokemonRepository
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

        binding.pbPokemonList.visibility = View.VISIBLE // Mostra o ProgressBar

        launch(coroutineContext)  {
            val pokemonsList = repository.getPokemonList(PAGE_SIZE, page * PAGE_SIZE)
            adapter = PokemonAdapter(pokemonsList)
            recyclerView.adapter = adapter
            binding.pbPokemonList.visibility = View.GONE // Esconde o ProgressBar
        }

        recyclerView.apply{
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1) && dy > 0)
                    {
                        launch {
                            binding.pbPokemonList.visibility = View.VISIBLE // Mostra o ProgressBar
                            page++
                            val pokemons = repository.getPokemonList(PAGE_SIZE, page * PAGE_SIZE)
                            adapter.updateData(pokemons.data!!)
                            binding.pbPokemonList.visibility = View.GONE // Esconde o ProgressBar
                        }
                    }
                    else {
                        super.onScrolled(recyclerView, dx, dy)
                    }
                }
            })
        }
    }
}