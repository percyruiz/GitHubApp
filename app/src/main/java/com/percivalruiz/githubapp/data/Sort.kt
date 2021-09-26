package com.percivalruiz.githubapp.data

/**
 * Type of sorting to be passed with search query
 */
enum class Sort(val position: Int, val key: String) {
  STARS(0,"stars"),
  FORKS(1,"forks"),
  ISSUES(2,"help-wanted-issues");

  companion object {
    fun fromPosition(position: Int): Sort =
      values().find { value -> value.position == position } ?: STARS

    fun getEnums() = enumValues<Sort>().map { it }
  }
}
