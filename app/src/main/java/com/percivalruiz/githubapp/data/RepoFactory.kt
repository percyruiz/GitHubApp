package com.percivalruiz.githubapp.data

import com.percivalruiz.githubapp.data.Repo

class RepoFactory {
  companion object {
    fun createRepo(dbid: Long, id: Long) = Repo(
      dbid = dbid,
      id = id,
      name = "repo $dbid",
      description = "repo $dbid description",
      url = "repo$dbid.com",
      stars = 100,
      forks = 100
    )
  }
}