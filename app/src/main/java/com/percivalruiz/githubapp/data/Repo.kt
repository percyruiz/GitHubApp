package com.percivalruiz.githubapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents list of repos from https://api.github.com/search/repositories
 */
@JsonClass(generateAdapter = true)
@Entity(tableName = "repo")
data class Repo(
  @PrimaryKey(autoGenerate = true) val dbid: Long? = null,
  val id: Long,
  val name: String,
  val description: String?,
  @Json(name = "html_url") val url: String,
  @Json(name = "stargazers_count") val stars: Int?,
  val forks: Int
)
