package com.genesis.obrio.data.repositories.search

import androidx.lifecycle.LiveData
import com.genesis.obrio.data.remote.entities.Repository

data class RepositoryResult(
    val repository: LiveData<MutableList<Repository>>,
    val networkErrors: LiveData<String>
)