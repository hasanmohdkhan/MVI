package com.hz.mvi.ui.main.state

sealed class MainStateEvent {

    class GetBlogPostEvent : MainStateEvent()

    class GetUser(val userId: String) : MainStateEvent()

    class None : MainStateEvent()

}