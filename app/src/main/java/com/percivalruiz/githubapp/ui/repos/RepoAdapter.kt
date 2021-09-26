package com.percivalruiz.githubapp.ui.repos

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.percivalruiz.githubapp.data.Repo

/**
 * Adapter for showing Repos
 * Extends [PagingDataAdapter] that handles paging using [Paging3] library
 */
class RepoAdapter(
  private val onClick: (url: String) -> Unit
) : PagingDataAdapter<Repo, RepoViewHolder>(ITEM_COMP) {

  override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
    return RepoViewHolder.create(parent, onClick)
  }

  companion object {
    private val ITEM_COMP = object : DiffUtil.ItemCallback<Repo>() {
      override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
        oldItem == newItem
    }
  }
}