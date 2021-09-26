package com.percivalruiz.githubapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.percivalruiz.githubapp.data.RemoteKey
import com.percivalruiz.githubapp.data.Repo

@Database(
  entities = [Repo::class, RemoteKey::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
  abstract fun repoDAO(): RepoDAO
  abstract fun remoteKeyDAO(): RemoteKeyDAO
}
