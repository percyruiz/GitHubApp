package com.percivalruiz.githubapp.ui.networkstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.percivalruiz.githubapp.R
import com.percivalruiz.githubapp.databinding.NetworkStateItemBinding

/**
 * ViewHolder implementation for showing load more and retry on footer
 */
class NetworkStateItemViewHolder(
  parent: ViewGroup,
  private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
  LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
) {
  private val binding = NetworkStateItemBinding.bind(itemView)
  private val progressBar = binding.progressBar
  private val errorMsg = binding.errorMsg
  private val retry = binding.retryButton
    .also {
      it.setOnClickListener { retryCallback() }
    }

  fun bindTo(loadState: LoadState) {
    progressBar.isVisible = loadState is LoadState.Loading
    retry.isVisible = loadState is LoadState.Error
    errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
    errorMsg.text = (loadState as? LoadState.Error)?.error?.message
  }
}
