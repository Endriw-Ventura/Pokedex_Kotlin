package com.example.pokedex.di.modules

import com.example.pokedex.consts.consts
import com.example.pokedex.data.remote.PokeApi
import com.example.pokedex.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
        fun providePokemonRepository(
            api: PokeApi
        ) = PokemonRepository(api)

    @Provides
    @Singleton
        fun providePokeApi() : PokeApi{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(consts.BASE_URL)
                .build()
                .create(PokeApi::class.java)
        }
}