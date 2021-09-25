package com.percivalruiz.githubapp.repository

import com.percivalruiz.githubapp.api.GitHubService
import com.percivalruiz.githubapp.data.Repo

interface SearchRepository {
  suspend fun searchRepo(query: String): List<Repo>
}

class SearchRepositoryImpl(private val service: GitHubService) : SearchRepository {

  override suspend fun searchRepo(query: String): List<Repo> {
    return service.searchRepositories(query = query).items
  }
}
