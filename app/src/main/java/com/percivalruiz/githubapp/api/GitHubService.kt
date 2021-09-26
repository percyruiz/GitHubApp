package com.percivalruiz.githubapp.api

import com.percivalruiz.githubapp.data.SearchRepoResponse
import com.percivalruiz.githubapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * GitHub API interface used in Retrofit
 */
interface GitHubService {

  @GET("search/repositories")
  suspend fun searchRepositories(
    @Query("q") query: String,
    @Query("sort") sort: String? = "stars",
    @Query("per_page") items: Int = Constants.NETWORK_ITEM_SIZE,
    @Query("page") page: Int = 1
  ): SearchRepoResponse
}
