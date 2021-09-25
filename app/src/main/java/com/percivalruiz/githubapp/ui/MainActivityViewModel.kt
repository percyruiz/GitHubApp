package com.percivalruiz.githubapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.percivalruiz.githubapp.data.Repo
import com.percivalruiz.githubapp.repository.SearchRepository
import kotlinx.coroutines.launch

/**
 * Contains logic for use cases in MainActivity, mainly for searching GitHub repositories
 */
class MainActivityViewModel(private val repository: SearchRepository) : ViewModel() {

  private val _repoLiveData = MutableLiveData<List<Repo>>()
  val repoLiveData = _repoLiveData

  fun search() {
    viewModelScope.launch {
      val repos = repository.searchRepo("android")
      _repoLiveData.postValue(repos)
    }
  }
}
