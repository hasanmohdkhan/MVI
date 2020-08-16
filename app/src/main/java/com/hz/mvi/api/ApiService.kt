package com.hz.mvi.api

import androidx.lifecycle.LiveData
import com.hz.mvi.model.BlogPost
import com.hz.mvi.model.User
import com.hz.mvi.utils.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId: String
    ): LiveData<GenericApiResponse<User>>

    @GET("placeholder/blogs")
    fun getBlogPost(): LiveData<GenericApiResponse<List<BlogPost>>>

}