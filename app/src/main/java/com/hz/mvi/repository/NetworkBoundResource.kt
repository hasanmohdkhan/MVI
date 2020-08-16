package com.hz.mvi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.hz.mvi.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject, ViewStateType> {
    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {
        result.value = DataState.loading(true)

        GlobalScope.launch(IO) {
            delay(1000)

            withContext(Main) {
                val apiResponse = createCall()
                result.addSource(apiResponse) { response ->
                    result.removeSource(apiResponse)

                    handleResponse(response)
                }
            }
        }
    }

    private fun handleResponse(response: GenericApiResponse<ResponseObject>) {
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                println("DEBUG : NetworkBoundResource : ${response.errorMessage}")
                onReturnError(response.errorMessage)
            }
            is ApiEmptyResponse -> {
                println("DEBUG: NetworkBoundResource: Request returned NOTHING (HTTP 204)")
                onReturnError("HTTP 204. Returned NOTHING.")
            }
        }
    }

    abstract fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    private fun onReturnError(errorMessage: String) {
        result.value = DataState.error(errorMessage)
    }

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
}