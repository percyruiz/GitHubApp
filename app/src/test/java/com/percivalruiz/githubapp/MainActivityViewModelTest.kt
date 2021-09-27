package com.percivalruiz.githubapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.jraska.livedata.test
import com.percivalruiz.githubapp.data.RepoFactory
import com.percivalruiz.githubapp.data.Search
import com.percivalruiz.githubapp.repository.SearchRepository
import com.percivalruiz.githubapp.ui.MainActivityViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainActivityViewModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val dispatcher = TestCoroutineDispatcher()
  private val QUERY = "com.percivalruiz.githubapp.QUERY"
  private val defaultSearch = Search(
    MainActivityViewModel.DEFAULT_QUERY,
    MainActivityViewModel.DEFAULT_SORT
  )
  private val repos = listOf(
    RepoFactory.createRepo(0, 0),
    RepoFactory.createRepo(1, 1),
    RepoFactory.createRepo(1, 1),
  )

  private lateinit var underTest: MainActivityViewModel

  @RelaxedMockK
  private lateinit var handle: SavedStateHandle

  @MockK
  private lateinit var repo: SearchRepository

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    Dispatchers.setMain(dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    dispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `should get list of repo on init with default search query and sort`() {
    // Mock init with default query and sort
    every { handle.contains(QUERY) } returns false
    every { handle.set(QUERY, any<Search>()) } just Runs
    every { repo.setCachedSearch(defaultSearch) } just Runs
    every { repo.getCachedSearch() } returns null
    every {
      handle.getLiveData<Search>(QUERY)
    } returns MutableLiveData(defaultSearch)
    coEvery { repo.searchRepo("kotlin", "stars") } returns flow {
      emit(PagingData.from(repos))
    }

    underTest = MainActivityViewModel(repo, handle)

    //assert itunes list is fetched
    underTest.repos.asLiveData().test()
      .assertHasValue()

    underTest.queryLiveData.test().equals(defaultSearch)
  }

  @Test
  fun `should be able to search with query and sort provided`() {
    // Mock init with default query and sort
    every { handle.contains(QUERY) } returns false
    every { handle.set(QUERY, any<Search>()) } just Runs
    every { repo.setCachedSearch(any()) } just Runs
    every { repo.getCachedSearch() } returns null
    every {
      handle.getLiveData<Search>(QUERY)
    } returns MutableLiveData(defaultSearch)
    coEvery { repo.searchRepo("kotlin", "stars") } returns flow {
      emit(PagingData.from(repos))
    }
    underTest = MainActivityViewModel(repo, handle)

    // Mock new values for query and sort for manual search
    val newSearch = Search("android", "forks")
    every {
      handle.getLiveData<Search>(QUERY)
    } returns MutableLiveData(newSearch)
    coEvery { repo.searchRepo("android", "forks") } returns flow {
      emit(PagingData.from(repos))
    }
    underTest.search("android", "forks")

    //assert itunes list is fetched
    underTest.repos.asLiveData().test()
      .assertHasValue()

    underTest.queryLiveData.test().equals(newSearch)
  }
}
