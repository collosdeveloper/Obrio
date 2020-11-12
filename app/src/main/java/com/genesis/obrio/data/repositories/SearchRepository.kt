package com.genesis.obrio.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.genesis.obrio.data.local.GithubLocalCache
import com.genesis.obrio.data.local.entity.RepositoryResult
import com.genesis.obrio.data.remote.GithubService
import com.genesis.obrio.data.remote.searchRepos

class SearchRepository(
    private val service: GithubService,
    private val cache: GithubLocalCache
) {
    @Volatile private var lastRequestedPage = 1
    private val networkErrors = MutableLiveData<String>()

    fun initNewSearch(query: String, limit: Int): RepositoryResult {
        Log.d("SearchRepository", "initNewSearch, new query: $query")
        lastRequestedPage = 1
        return RepositoryResult(cache.reposByName(query, limit), networkErrors)
    }

    fun searchRequest(query: String) {
        Log.d("SearchRepository", "searchRequest start, query: $query")
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos) {
                Log.d("SearchRepository", "searchRequest, repos size: ${repos.size}")
            }
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