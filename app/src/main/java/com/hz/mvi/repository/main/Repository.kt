package com.hz.mvi.repository.main


import androidx.lifecycle.LiveData
import com.hz.mvi.api.RetrofitBuilder
import com.hz.mvi.model.BlogPost
import com.hz.mvi.model.User
import com.hz.mvi.repository.NetworkBoundResource
import com.hz.mvi.ui.main.state.MainViewState
import com.hz.mvi.utils.ApiSuccessResponse
import com.hz.mvi.utils.DataState
import com.hz.mvi.utils.GenericApiResponse

object Repository {

    fun getBlogPost(): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<List<BlogPost>, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(
                    data = MainViewState(
                        blogPosts = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return RetrofitBuilder.apiService.getBlogPost()
            }

        }.asLiveData()
    }

    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<User, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    data = MainViewState(
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()
    }

}