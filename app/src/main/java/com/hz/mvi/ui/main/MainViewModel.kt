package com.hz.mvi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hz.mvi.model.BlogPost
import com.hz.mvi.model.User
import com.hz.mvi.repository.main.Repository
import com.hz.mvi.ui.main.state.MainStateEvent
import com.hz.mvi.ui.main.state.MainStateEvent.*
import com.hz.mvi.ui.main.state.MainViewState
import com.hz.mvi.utils.AbsentLiveData
import com.hz.mvi.utils.DataState

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()

    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()
    val viewState: LiveData<MainViewState>
        get() = _viewState

    val dataState: LiveData<DataState<MainViewState>> =
        Transformations.switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
        }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        when (stateEvent) {
            is GetBlogPostEvent -> {
                return Repository.getBlogPost()
            }
            is GetUser -> {
                return Repository.getUser(stateEvent.userId)
            }
            is None -> {
                return AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update

    }

    fun setUser(user: User) {
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update

    }

    fun getCurrentViewStateOrNew(): MainViewState {
        return viewState.value?.let {
            it
        } ?: MainViewState()

    }

    fun setStateEvent(event: MainStateEvent) {
        _stateEvent.value = event
    }

}