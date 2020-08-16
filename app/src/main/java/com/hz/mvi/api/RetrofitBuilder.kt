package com.hz.mvi.api

import com.hz.mvi.utils.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    const val BASE_URL: String = "https://open-api.xyz/"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
    }

    val apiService: ApiService by lazy {
        retrofitBuilder.build().create(ApiService::class.java)
    }


}