package com.example.rove.models

data class Album(
    val title: String?,
    val year: String?,
    val music: MutableList<Music>?,
    val totalDuration: Long
    )
