package com.backpapp.gvttest.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    private val _sharedFlow = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun navigateTo(screen: NavigationEvent) {
        _sharedFlow.tryEmit(screen)
    }
}