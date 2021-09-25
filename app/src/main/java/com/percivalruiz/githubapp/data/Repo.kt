package com.percivalruiz.githubapp.data

/**
 * Represents list of repos from https://api.github.com/search/repositories
 */
data class Repo(
  val id: Long,
  val name: String,
  val description: String?,
  val url: String,
  val stars: Int?,
  val forks: Int
)
