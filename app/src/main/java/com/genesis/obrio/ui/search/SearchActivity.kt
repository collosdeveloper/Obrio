package com.genesis.obrio.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.genesis.obrio.R
import com.genesis.obrio.databinding.ActivitySearchBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SearchActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: SearchViewModelFactory by instance()

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    lateinit var searchView: SearchView
    private val adapter = RepositoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarTitle.text = getString(R.string.toolbar_search_repository_title)
        binding.toolbar.inflateMenu(R.menu.menu_repository_search)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
        setupScrollListener()

        initAdapter()
    }

    private fun initAdapter() {
        binding.list.adapter = adapter
        viewModel.repos.observe(this, {
            binding.progressBar.visibility = View.GONE
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(this, {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_repository_search, menu)
        menu.findItem(R.id.menu_search).let { searchActionMenuItem ->
            searchView = searchActionMenuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (!searchView.isIconified) {
                        searchView.isIconified = true
                    }
                    updateRepoListFromInput(query)
                    searchActionMenuItem.collapseActionView()
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean = false
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = true

    private fun updateRepoListFromInput(query: String) {
        query.trim().let {
            if (it.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                binding.list.scrollToPosition(0)
                viewModel.searchRepo(it)
                adapter.submitList(null)
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        binding.emptyList.visibility = if (show) View.VISIBLE else View.GONE
        binding.list.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun setupScrollListener() {
        binding.list.let { rv ->
            val layoutManager = rv.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            rv.addOnScrollListener(object :
                androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: androidx.recyclerview.widget.RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = layoutManager.itemCount
                    val visibleItemCount = layoutManager.childCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
                }
            })
        }
    }
}