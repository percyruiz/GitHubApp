package com.percivalruiz.githubapp.data

/**
 * Represents response from https://api.github.com/search/repositories
 */
data class SearchRepoResponse(
  val total: Int = 0,
  val items: List<Repo> = emptyList()
)
