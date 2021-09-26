package com.percivalruiz.githubapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.percivalruiz.githubapp.api.GitHubService
import com.percivalruiz.githubapp.data.Repo
import com.percivalruiz.githubapp.db.AppDatabase
import com.percivalruiz.githubapp.util.Constants
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
  suspend fun searchRepo(query: String): List<Repo>
  suspend fun searchRepo(): Flow<PagingData<Repo>>
}

/**
 * Data source class for [Repo]
 *
 * @property service Interface to API service
 * @property db App's database instance
 */
class SearchRepositoryImpl(private val service: GitHubService, private val db: AppDatabase) :
  SearchRepository {

  override suspend fun searchRepo(query: String): List<Repo> {
    return service.searchRepositories(query = query).items
  }

  @ExperimentalPagingApi
  override suspend fun searchRepo() = Pager(
    config = PagingConfig(Constants.NETWORK_ITEM_SIZE),
    remoteMediator = RepoMediator(db, service)
  ) {
    db.repoDAO().getAll()
  }.flow
}
