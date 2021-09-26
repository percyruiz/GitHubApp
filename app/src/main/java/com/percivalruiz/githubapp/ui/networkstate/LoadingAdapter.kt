package com.percivalruiz.githubapp.ui.networkstate

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.percivalruiz.githubapp.ui.repos.RepoAdapter

/**
 * Adapter for showing loading and error status in footer
 */
class LoadingAdapter(
  private val adapter: RepoAdapter
) : LoadStateAdapter<NetworkStateItemViewHolder>() {
  override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
    holder.bindTo(loadState)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    loadState: LoadState
  ): NetworkStateItemViewHolder {
    return NetworkStateItemViewHolder(parent) { adapter.retry() }
  }
}
