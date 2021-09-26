package com.percivalruiz.githubapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search(val query: String, val sort: String) : Parcelable
