package com.percivalruiz.githubapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.percivalruiz.githubapp.api.GitHubService
import com.percivalruiz.githubapp.data.Repo
import com.percivalruiz.githubapp.data.Search
import com.percivalruiz.githubapp.data.SearchQueryPrefs
import com.percivalruiz.githubapp.db.AppDatabase
import com.percivalruiz.githubapp.util.Constants
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
  suspend fun searchRepo(query: String, sort: String): Flow<PagingData<Repo>>
  fun getCachedSearch(): Search?
  fun setCachedSearch(search: Search)
}

/**
 * Data source class for [Repo]
 *
 * @property service Interface to API service
 * @property db App's database instance
 */
class SearchRepositoryImpl(
  private val service: GitHubService,
  private val db: AppDatabase,
  private val prefs: SearchQueryPrefs
) : SearchRepository {

  @ExperimentalPagingApi
  override suspend fun searchRepo(query: String, sort: String) = Pager(
    config = PagingConfig(Constants.NETWORK_ITEM_SIZE),
    remoteMediator = RepoMediator(db, service, query, sort)
  ) {
    db.repoDAO().getAll()
  }.flow

  override fun getCachedSearch(): Search? {
    return if(prefs.query.isNullOrBlank() || prefs.sort.isNullOrBlank()) {
      null
    } else {
      Search(prefs.query!!, prefs.sort!!)
    }
  }

  override fun setCachedSearch(search: Search) {
    prefs.query = search.query
    prefs.sort = search.sort
  }
}
