package com.genesis.obrio.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.genesis.obrio.data.remote.entities.Repository
import com.genesis.obrio.databinding.ItemRepositoryBinding

class RepositoryViewHolder(private val binding: ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: Repository?) {
        binding.repo = repo
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
            return RepositoryViewHolder(binding as ItemRepositoryBinding)
        }
    }
}