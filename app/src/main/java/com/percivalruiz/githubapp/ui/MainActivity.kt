package com.percivalruiz.githubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.percivalruiz.githubapp.data.Repo
import com.percivalruiz.githubapp.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainActivityViewModel by viewModel()
  val repos = mutableListOf<Repo>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    viewModel.search()

    val adapter = RepoAdapter(repos)
    binding.recyclerview.run {
      this.adapter = adapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    viewModel.repoLiveData.observe(this) {
      repos.clear()
      repos.addAll(it)
      adapter.notifyDataSetChanged()
    }

  }
}