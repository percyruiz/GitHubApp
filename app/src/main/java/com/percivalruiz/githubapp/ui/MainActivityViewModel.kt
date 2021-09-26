package com.percivalruiz.githubapp.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.percivalruiz.githubapp.data.Search
import com.percivalruiz.githubapp.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Contains logic for use cases in MainActivity, mainly for searching GitHub repositories
 * For every search invoke, it will:
 *    update the SavedStateHandle that will trigger an endpoint call
 *    save the query and sort Strings to SharedPreference for persistence
 */
class MainActivityViewModel(
  private val repository: SearchRepository,
  private val handle: SavedStateHandle
) : ViewModel() {

  private val defaultSearch = Search(DEFAULT_QUERY, DEFAULT_SORT)

  private val _queryLiveData = MutableLiveData<Search>()
  val queryLiveData: LiveData<Search> = _queryLiveData

  /**
   * Checks pref cache for last search query and sort, used the cached values on next app open
   */
  init {
    val queryToUi = if(!handle.contains(QUERY) && repository.getCachedSearch() == null) {
      handle.set(QUERY, defaultSearch)
      repository.run {
        setCachedSearch(defaultSearch)
        getCachedSearch()
      }
    } else {
      val query = repository.getCachedSearch()
      handle.set(QUERY, query)
      query
    }

    _queryLiveData.value = queryToUi ?: defaultSearch
  }

  /**
   * Observe changes from the handle as a [LiveData] object
   * This call service's get repo method on [LiveData] updates
   */
  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  val repos = handle.getLiveData<Search>(QUERY)
    .asFlow()
    .flatMapLatest {
      repository.searchRepo(it.query, it.sort)
    }
    .cachedIn(viewModelScope)

  fun search(query: String, sort: String) {
    val search = Search(query, sort)
    handle.set(QUERY, search)
    repository.setCachedSearch(search)
  }

  companion object {
    const val QUERY = "com.percivalruiz.githubapp.QUERY"
    const val DEFAULT_QUERY = "kotlin"
    const val DEFAULT_SORT = "stars"
  }
}
