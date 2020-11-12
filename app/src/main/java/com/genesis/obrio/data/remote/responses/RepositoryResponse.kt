package com.genesis.obrio.data.remote.responses

import com.genesis.obrio.data.local.entity.Repository
import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<Repository> = emptyList(),
    val nextPage: Int? = null
)