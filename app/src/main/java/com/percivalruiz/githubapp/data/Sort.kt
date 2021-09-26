package com.percivalruiz.githubapp.data

/**
 * Type of sorting to be passed with search query
 */
enum class Sort(val key: String) {
  STARS("stars"),
  FORKS("forks"),
  ISSUES("help-wanted-issues")
}
