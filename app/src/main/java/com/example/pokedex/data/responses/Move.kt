package com.example.pokedex.data.responses

import java.io.Serializable

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
) : Serializable