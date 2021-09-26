package com.percivalruiz.githubapp.ui.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.percivalruiz.githubapp.R
import com.percivalruiz.githubapp.data.Repo

/**
 * Populates the ITunes list item
 */
class RepoViewHolder(
  view: View,
  onClick: (id: String) -> Unit,
) : RecyclerView.ViewHolder(view) {

  private val name: TextView = view.findViewById(R.id.name_textview)
  private val description: TextView = view.findViewById(R.id.description_textview)
  private val stars: TextView = view.findViewById(R.id.star_textview)
  private val fork: TextView = view.findViewById(R.id.fork_textview)
  private var repo: Repo? = null

  init {
    view.setOnClickListener {
      onClick.invoke(repo?.url.orEmpty())
    }
  }

  fun bind(repo: Repo?) {
    repo?.apply {
      this@RepoViewHolder.repo = this
      this@RepoViewHolder.name.text = this.name
      this@RepoViewHolder.description.text = this.description
      this@RepoViewHolder.stars.text = this.stars.toString()
      this@RepoViewHolder.fork.text = this.forks.toString()
    }
  }

  companion object {
    fun create(parent: ViewGroup, onClick: (id: String) -> Unit): RepoViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent, false)
      return RepoViewHolder(view, onClick)
    }
  }
}