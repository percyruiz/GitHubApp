package com.percivalruiz.githubapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.percivalruiz.githubapp.data.Sort
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

    val sortBy = Sort.getEnums().map { it.key }
    binding.sortAutoComplete.setAdapter(
      ArrayAdapter(
        this,
        android.R.layout.simple_dropdown_item_1line,
        sortBy
      )
    )

    initRepoAdapter()

    binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        if (binding.searchEditText.text.isNotBlank()) {
          viewModel.search(
            binding.searchEditText.text.toString(),
            binding.sortAutoComplete.text.toString()
          )
          binding.list.smoothScrollToPosition(0)
        } else {
          Toast.makeText(this, "Search term is blank", Toast.LENGTH_LONG).show()
        }
        hideKeyboard()
        true
      }
      false
    }

    binding.sortAutoComplete.setOnItemClickListener { _, _, position, _ ->
      viewModel.search(
        query = binding.searchEditText.text.toString(),
        sort = Sort.fromPosition(position).key
      )
      binding.list.smoothScrollToPosition(0)
    }

    viewModel.queryLiveData.observe(this) {
      binding.searchEditText.setText(it.query)
      binding.sortAutoComplete.setText(it.sort, false)
    }
  }

  private fun initRepoAdapter() {
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
          loadStates.refresh is LoadState.Loading && loadStates.append !is LoadState.Loading

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

  private fun hideKeyboard(): Boolean {
    return (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
      .hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
  }
}
