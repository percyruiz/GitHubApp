package com.percivalruiz.githubapp.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.percivalruiz.githubapp.data.Repo

@Dao
interface RepoDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(vararg repo: Repo)

  @Query("SELECT * FROM repo")
  fun getAll(): PagingSource<Int, Repo>

  @Query("DELETE FROM repo")
  fun nuke()
}
