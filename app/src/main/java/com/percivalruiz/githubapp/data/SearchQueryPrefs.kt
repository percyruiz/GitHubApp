package com.percivalruiz.githubapp.data

import android.content.SharedPreferences

interface SearchQueryPrefs {
  var query: String?
  var sort: String?
}

class SearchQueryPrefsImpl(private val prefs: SharedPreferences): SearchQueryPrefs {
  override var query: String?
    set(value) {
      with (prefs.edit()) {
        putString(QUERY, value)
        apply()
      }
    }
    get() {
      return prefs.getString(QUERY, "")
    }

  override var sort: String?
    set(value) {
      with (prefs.edit()) {
        putString(SORT, value)
        apply()
      }
    }
    get() {
      return prefs.getString(SORT, "")
    }

  companion object {
    private const val QUERY = "com.percivalruiz.githubapp.data.QUERY"
    private const val SORT = "com.percivalruiz.githubapp.data.SORT"
  }
}
