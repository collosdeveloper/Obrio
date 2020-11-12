package com.genesis.obrio.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import com.genesis.obrio.data.local.entity.Repository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GithubLocalCache(
    database: RepositoryDatabase
) {
    private var repoDao: RepositoryDao = database.repositoryDao()
    private var ioExecutor: Executor = Executors.newSingleThreadExecutor()

    fun insert(repos: List<Repository>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    fun reposByName(name: String, limit: Int): LiveData<List<Repository>> {
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query, limit)
    }
}