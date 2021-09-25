package com.percivalruiz.githubapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.percivalruiz.githubapp.R
import com.percivalruiz.githubapp.data.Repo

class RepoAdapter(private val repos: List<Repo>): RecyclerView.Adapter<RepoAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.nameTextView.text = repos[position].name
  }

  override fun getItemCount(): Int = repos.size

  class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameTextView: TextView = view.findViewById(R.id.name_textview)
  }
}