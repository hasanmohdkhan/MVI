package com.hz.mvi.ui.main

import com.hz.mvi.utils.DataState

interface DataStateListener {
    fun onDataStateChange(dataState: DataState<*>?)
}