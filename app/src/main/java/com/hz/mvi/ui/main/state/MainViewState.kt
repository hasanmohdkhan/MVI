package com.hz.mvi.ui.main.state

import com.hz.mvi.model.BlogPost
import com.hz.mvi.model.User

/*
* This class wrapper for every possible state
* */
data class MainViewState(
    var blogPosts: List<BlogPost>? = null,
    var user: User? = null
)