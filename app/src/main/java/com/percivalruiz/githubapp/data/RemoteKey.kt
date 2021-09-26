package com.percivalruiz.githubapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the key used for fetching paginated list from https://api.github.com/search/repositories
 */
@Entity(tableName = "remote_key")
data class RemoteKey(
  @PrimaryKey val id: Int = 0,
  @ColumnInfo(name = "page") val page: Int
)
