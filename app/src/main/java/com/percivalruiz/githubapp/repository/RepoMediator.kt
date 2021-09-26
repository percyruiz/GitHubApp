package com.percivalruiz.githubapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.percivalruiz.githubapp.api.GitHubService
import com.percivalruiz.githubapp.data.RemoteKey
import com.percivalruiz.githubapp.data.Repo
import com.percivalruiz.githubapp.db.AppDatabase
import com.percivalruiz.githubapp.util.Constants
import retrofit2.HttpException
import java.io.IOException

/**
 * RemoteMediator class used for Paging
 * Extends Android's [RemoteMediator] class
 * This handles loading of data from service and saving it on db
 * LoadType are passed to the load method which corresponds to the status of the adapter
 *
 * @property db App's database instance
 * @property service Interface to API service
 */
@ExperimentalPagingApi
class RepoMediator(
  private val db: AppDatabase,
  private val service: GitHubService
) : RemoteMediator<Int, Repo>() {

  private val repoDAO = db.repoDAO()
  private val remoteKeyDAO = db.remoteKeyDAO()

  override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
    try {
      val page = when (loadType) {
        LoadType.REFRESH -> null
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        LoadType.APPEND -> {
          // Get the page value from db
          val remoteKey = db.withTransaction { remoteKeyDAO.peek() }
          remoteKey?.page ?: Constants.GITHUB_INITIAL_PAGE
        }
      }

      // Call search endpoint
      val response =
        service.searchRepositories(query = "android", page = page ?: Constants.GITHUB_INITIAL_PAGE)

      db.withTransaction {
        // Remove Repo data and RemoteKey data saved in db cache when refresh is being called
        if (loadType == LoadType.REFRESH) {
          repoDAO.nuke()
          remoteKeyDAO.nuke()
        }

        // Update next page in remote_key table
        val currentPage = remoteKeyDAO.peek()?.page ?: Constants.GITHUB_INITIAL_PAGE
        remoteKeyDAO.insert(RemoteKey(id = 0, page = currentPage + 1))

        // Cache to db
        repoDAO.insert(*(response.items).toTypedArray())
      }

      // Set end of page if there is no more data being fetched and if search criteria is blank
      return MediatorResult.Success(endOfPaginationReached = response.items.isEmpty())
    } catch (e: IOException) {
      return MediatorResult.Error(e)
    } catch (e: HttpException) {
      return MediatorResult.Error(e)
    }
  }
}
