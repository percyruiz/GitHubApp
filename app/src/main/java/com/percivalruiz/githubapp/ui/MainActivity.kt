package com.percivalruiz.githubapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.percivalruiz.githubapp.databinding.ActivityMainBinding
import com.percivalruiz.githubapp.ui.networkstate.LoadingAdapter
import com.percivalruiz.githubapp.ui.repos.RepoAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainActivityViewModel by stateViewModel()
  private val adapter = RepoAdapter(::goToRepoUrl)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    binding.retryButton.setOnClickListener { adapter.retry() }

    // Set up adapter footer, header, and layout
    binding.list.apply {
      adapter = this@MainActivity.adapter.withLoadStateFooter(
        footer = LoadingAdapter(this@MainActivity.adapter)
      )

      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    // Handles updating the list
    lifecycleScope.launchWhenCreated {
      viewModel.repos.collectLatest {
        adapter.submitData(it)
      }
    }

    // Updates progressbar, refresh, error toast by observing [Flow<CombinedLoadStates>]
    lifecycleScope.launch {
      adapter.loadStateFlow.collectLatest { loadStates ->
        binding.retryButton.isVisible =
          loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0 && loadStates.refresh is LoadState.Error

        binding.progressBar.isVisible =
          adapter.itemCount == 0 && loadStates.refresh is LoadState.Loading

        if (loadStates.refresh !is LoadState.Loading && loadStates.refresh is LoadState.Error) {
          Toast.makeText(
            this@MainActivity,
            (loadStates.refresh as? LoadState.Error)?.error?.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  private fun goToRepoUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
    startActivity(intent)
  }
}