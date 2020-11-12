package com.genesis.obrio.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.genesis.obrio.R
import com.genesis.obrio.data.local.entity.Repository
import com.genesis.obrio.databinding.ItemRepositoryBinding

class RepositoryViewHolder(private val binding: ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root) {
    private var repo: Repository? = null

    fun bind(repo: Repository?) {
        if (repo == null) {
            val resources = itemView.resources
            binding.repoName.text = resources.getString(R.string.loading)
            binding.repoFullname.visibility = View.GONE
            binding.repoStars.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Repository) {
        this.repo = repo
        binding.repoName.text = repo.name
        binding.repoFullname.text = repo.fullName
        binding.repoStars.text = repo.stars.toString()
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
            return RepositoryViewHolder(binding as ItemRepositoryBinding)
        }
    }
}