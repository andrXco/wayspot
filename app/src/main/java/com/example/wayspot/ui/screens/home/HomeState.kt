package com.example.wayspot.ui.screens.home

import com.example.wayspot.data.model.Post

data class HomeState(
    val searchText: String = "",
    val posts: List<Post> = emptyList()
)
