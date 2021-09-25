package com.percivalruiz.githubapp.di

import android.util.Log
import com.percivalruiz.githubapp.api.GitHubService
import com.percivalruiz.githubapp.repository.SearchRepository
import com.percivalruiz.githubapp.repository.SearchRepositoryImpl
import com.percivalruiz.githubapp.ui.MainActivityViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Handles dependency injection using Koin
 */
val appModule = module {

  single {
    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
  }

  single {
    val logger = HttpLoggingInterceptor { Log.d("API", it) }
    logger.level = HttpLoggingInterceptor.Level.BODY
    OkHttpClient.Builder().addInterceptor(logger).build()
  }

  single<GitHubService> {
    val retrofit = Retrofit.Builder()
      .baseUrl("https://api.github.com/")
      .client(get())
      .addConverterFactory(MoshiConverterFactory.create(get()))
      .build()
    retrofit.create(GitHubService::class.java)
  }

  // -- Repository --

  single<SearchRepository> { SearchRepositoryImpl(service = get()) }

  // -- ViewModel --

  viewModel { MainActivityViewModel(repository = get()) }
}
