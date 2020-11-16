package com.genesis.obrio

import android.app.Application
import com.genesis.obrio.data.remote.GithubService
import com.genesis.obrio.data.repositories.search.SearchRepository
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

        bind() from singleton { GithubService() }
        bind() from singleton { SearchRepository(instance()) }
        bind() from provider { SearchViewModelFactory(instance(), instance()) }
    }
}