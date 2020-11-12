package com.genesis.obrio

import android.app.Application
import com.genesis.obrio.data.local.GithubLocalCache
import com.genesis.obrio.data.local.RepositoryDatabase
import com.genesis.obrio.data.network.NetworkConnectionInterceptor
import com.genesis.obrio.data.remote.GithubService
import com.genesis.obrio.data.repositories.SearchRepository
import com.genesis.obrio.ui.search.SearchViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ObrioApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@ObrioApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { RepositoryDatabase(instance()) }
        bind() from singleton { GithubLocalCache(instance()) }
        bind() from singleton { GithubService() }
        bind() from singleton { SearchRepository(instance(), instance()) }
        bind() from provider { SearchViewModelFactory(instance(), instance()) }
    }
}