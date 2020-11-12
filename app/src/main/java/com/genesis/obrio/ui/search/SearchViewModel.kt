package com.genesis.obrio.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.genesis.obrio.data.local.entity.Repository
import com.genesis.obrio.data.local.entity.RepositoryResult
import com.genesis.obrio.data.repositories.SearchRepository
import kotlinx.coroutines.*

class SearchViewModel(
    private val repository: SearchRepository,
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
        private const val MAX_ITEM_COUNT = 30
        private const val THREAD_COUNT = 2
    }

    private val queryLiveData = MutableLiveData<String>()
    private val repoResult: MutableLiveData<RepositoryResult> = MutableLiveData()

    val repos: LiveData<List<Repository>> = Transformations.switchMap(repoResult) { it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) {
        it.networkErrors
    }

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
        repoResult.postValue(repository.initNewSearch(queryString, MAX_ITEM_COUNT))
        doSearch(queryString)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        Log.d("listScrolled", "totalItemCount: $totalItemCount")
        if (totalItemCount >= MAX_ITEM_COUNT) return
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = lastQueryValue()
            if (immutableQuery != null) {
                doSearch(immutableQuery)
            }
        }
    }

    private fun doSearch(queryString: String) {
        viewModelScope.launch {
            val jobs: MutableList<Deferred<Unit>> = mutableListOf()
            repeat(THREAD_COUNT) {
                jobs.add(async { repository.searchRequest(queryString) })
            }
            for (job: Deferred<Unit> in jobs) {
                job.await()
            }
        }
    }

    private fun lastQueryValue(): String? = queryLiveData.value
}