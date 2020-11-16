package com.genesis.obrio.data.remote.entities

import com.google.gson.annotations.SerializedName

data class Repository(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("full_name") val fullName: String,
    @field:SerializedName("stargazers_count") val stars: Int
)