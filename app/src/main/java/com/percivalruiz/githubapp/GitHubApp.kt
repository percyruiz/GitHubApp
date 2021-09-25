package com.percivalruiz.githubapp

import android.app.Application
import com.percivalruiz.githubapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GitHubApp: Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@GitHubApp)
      modules(appModule)
    }
  }
}
