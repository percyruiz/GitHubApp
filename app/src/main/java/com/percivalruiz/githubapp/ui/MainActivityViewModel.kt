package com.percivalruiz.githubapp.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.percivalruiz.githubapp.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Contains logic for use cases in MainActivity, mainly for searching GitHub repositories
 */
class MainActivityViewModel(
  private val repository: SearchRepository,
  private val handle: SavedStateHandle
) : ViewModel() {

  init {
    if(!handle.contains(QUERY)) {
      handle.set(QUERY, "android")
    }
  }

  /**
   * Observe changes from the handle as a [LiveData] object
   * This call service's get repo method on [LiveData] updates
   */
  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  val repos = handle.getLiveData<String>(QUERY)
    .asFlow()
    .flatMapLatest {
      repository.searchRepo()
    }
    .cachedIn(viewModelScope)

  fun search(query: String) {
    handle.set(QUERY, query)
  }

  companion object {
    const val QUERY = "com.percivalruiz.githubapp.QUERY"
  }
}
