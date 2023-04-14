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
import com.example.pokedex.util.PokemonUtil
import com.example.pokedex.util.Resource
import java.io.Serializable
import java.util.*


class PokemonAdapter(
    private var pokemons: Resource<MutableList<PokemonData>>
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>(), Serializable {
    inner class PokemonViewHolder(val binding: PokemonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonItemBinding.inflate(inflater, parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.binding.apply {
            val colorTypeInt = getColorInt(position)
            tvPokemonName.text = PokemonUtil.turnInCamelCase(pokemons.data!![position].name)
            cvPokemonItem.backgroundTintList =
                ContextCompat.getColorStateList(root.context, colorTypeInt)
            pbPokemonItem.visibility = View.VISIBLE
            loadPokemonImage(position)
        }
        configureOnClickListener(holder, position)
    }

    private fun getColorInt(position: Int): Int {
        val colorName = pokemons.data!![position].types.first().type.name
        return PokemonTypes.getTypeColor(colorName)
    }

    private fun PokemonItemBinding.loadPokemonImage(position: Int) {
        ivPokemonImage.load(PokemonUtil.getPokemonUrl(pokemons.data!![position].id.toString())) {
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

    private fun configureOnClickListener(
        holder: PokemonViewHolder,
        position: Int
    ) {
        holder.itemView.setOnClickListener {
            val myIntent = Intent(holder.binding.root.context, PokeDetailActivity::class.java)
                .apply {
                    putExtra("POKEMON", pokemons.data!![position])
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
}