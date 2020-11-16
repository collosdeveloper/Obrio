package com.genesis.obrio.ui.search

import android.app.Application
import androidx.lifecycle.*
import com.genesis.obrio.data.remote.entities.Repository
import com.genesis.obrio.data.repositories.search.RepositoryResult
import com.genesis.obrio.data.repositories.search.SearchRepository
import kotlinx.coroutines.*

class SearchViewModel(
    private val repository: SearchRepository,
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val THREAD_COUNT = 2
    }

    private val repoResult: MutableLiveData<RepositoryResult> = MutableLiveData()

    val repos: LiveData<MutableList<Repository>> = Transformations.switchMap(repoResult) { it.repository }
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) { it.networkErrors }

    fun searchRepo(queryString: String) {
        viewModelScope.launch {
            repoResult.postValue(repository.initNewSearch(queryString))
            val jobs: List<Deferred<Unit>> = (1..THREAD_COUNT).map {
                async { repository.makeSearchRequest(queryString) }
            }
            jobs.awaitAll()
        }
    }
}