package com.genesis.obrio.data.repositories.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.genesis.obrio.data.remote.GithubService
import com.genesis.obrio.data.remote.callSearchReposRequest
import com.genesis.obrio.data.remote.entities.Repository

class SearchRepository(
    private val service: GithubService
) {
    @Volatile private var lastRequestedPage = 1
    private val repository = MutableLiveData<MutableList<Repository>>()
    private val networkErrors = MutableLiveData<String>()

    fun initNewSearch(query: String): RepositoryResult {
        Log.d("SearchRepository", "initNewSearch, new query: $query")
        lastRequestedPage = 1
        repository.value?.clear()
        return RepositoryResult(repository, networkErrors)
    }

    fun makeSearchRequest(query: String) {
        Log.d("SearchRepository", "searchRequest start, query: $query")
        callSearchReposRequest(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            repository.postValue((repository.value ?: mutableListOf()).apply {
                addAll(repos)
                sortByDescending { it.stars }
            })
            Log.d("SearchRepository", "searchRequest, repos size: ${repos.size}")
        }, { error ->
            lastRequestedPage--
            networkErrors.postValue(error)
        })
        Log.d("SearchRepository", "searchRequest end, query: $query")
        lastRequestedPage++
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 15
    }
}