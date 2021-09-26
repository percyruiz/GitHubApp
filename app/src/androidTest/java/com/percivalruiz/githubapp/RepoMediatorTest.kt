package com.percivalruiz.githubapp

import android.content.Context
import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.percivalruiz.githubapp.api.GitHubService
import com.percivalruiz.githubapp.data.Repo
import com.percivalruiz.githubapp.data.RepoFactory
import com.percivalruiz.githubapp.data.SearchRepoResponse
import com.percivalruiz.githubapp.db.AppDatabase
import com.percivalruiz.githubapp.repository.RepoMediator
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RepoMediatorTest {

  @MockK
  private lateinit var service: GitHubService
  @MockK
  private lateinit var httpException: HttpException

  private lateinit var db: AppDatabase
  private lateinit var underTest: RepoMediator

  private val response = SearchRepoResponse(
    3,
    listOf(
      RepoFactory.createRepo(0, 0),
      RepoFactory.createRepo(1, 1),
      RepoFactory.createRepo(1, 1),
    )
  )

  private val emptyResponse = SearchRepoResponse(
    0,
    mutableListOf()
  )

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(
      context, AppDatabase::class.java).build()
    underTest = RepoMediator(
      db,
      service,
      "test",
      "test"
    )
  }

  @Test
  fun refreshLoadReturnsSuccessResult() = runBlocking {
    // Add mock results for the API to return.
    coEvery { service.searchRepositories(query = "test", sort = "test") } returns response

    val pagingState = PagingState<Int, Repo>(
      listOf(),
      null,
      PagingConfig(10),
      10
    )
    val result = underTest.load(LoadType.REFRESH, pagingState)
    Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
    Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
  }

  @Test
  fun refreshLoadSuccessAndEndOfPagination() = runBlocking {
    // Add mock results for the API to return.
    coEvery { service.searchRepositories(query = "test", sort = "test") } returns emptyResponse

    val pagingState = PagingState<Int, Repo>(
      listOf(),
      null,
      PagingConfig(10),
      10
    )
    val result = underTest.load(LoadType.REFRESH, pagingState)
    Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
    Assert.assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
  }

  @Test
  fun refreshLoadReturnsErrorResult() = runBlocking {
    // Set up failure message to throw exception from the mock API.
    coEvery { service.searchRepositories(query = "test", sort = "test") } throws httpException
    val pagingState = PagingState<Int, Repo>(
      listOf(),
      null,
      PagingConfig(10),
      10
    )
    val result = underTest.load(LoadType.REFRESH, pagingState)
    Assert.assertTrue(result is RemoteMediator.MediatorResult.Error)
  }
}
