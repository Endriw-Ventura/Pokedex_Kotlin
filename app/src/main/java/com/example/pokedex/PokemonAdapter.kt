package com.example.pokedex

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokedex.consts.consts
import com.example.pokedex.data.responses.PokemonData
import com.example.pokedex.databinding.PokemonItemBinding
import com.example.pokedex.util.PokemonTypes
import com.example.pokedex.util.Resource
import java.util.*


class PokemonAdapter(
    private var pokemons: Resource<MutableList<PokemonData>>
): RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    inner class PokemonViewHolder(val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonItemBinding.inflate(inflater, parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.binding.apply {
            tvPokemonName.text = pokemons.data!![position].name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }

            val colorName = pokemons.data!![position].types.first().type.name
            val colorTypeInt = PokemonTypes.getTypeColor(colorName)

            cvPokemonItem.backgroundTintList =
                ContextCompat.getColorStateList(holder.binding.root.context, colorTypeInt)
            pbPokemonItem.visibility = View.VISIBLE
            ivPokemonImage.load(
                consts.IMG_URL.replace(
                    "idPokemon",
                    pokemons.data!![position].id.toString()
                )
            ) {
                //Listener para esconder o ProgressBar quando a imagem é carregada com sucesso
                listener(
                    onSuccess = { request, metadata ->
                        pbPokemonItem.visibility = View.GONE
                        ivPokemonImage.visibility = View.VISIBLE
                    },
                    onError = { request, throwable ->
                        pbPokemonItem.visibility = View.GONE
                        ivPokemonImage.visibility = View.VISIBLE
                    }
                )
            }
        }

        holder.itemView.setOnClickListener{
            val myIntent = Intent(holder.binding.root.context, PokeDetailActivity::class.java)
                .apply {
                    putExtra("POKEMON_NAME", pokemons.data!![position].name)
                }
            holder.binding.root.context.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int {
        return pokemons.data!!.size
    }


    fun updateData(pokemonsList: MutableList<PokemonData>) {
        val index = pokemons.data!!.size - 1
        pokemons.data!!.addAll(pokemonsList)
        notifyItemRangeInserted(index, pokemonsList.size)
    }

    private fun getPokemonUrl(url: String): String {
        return consts.IMG_URL.replace("idPokemon", url)
    }
}