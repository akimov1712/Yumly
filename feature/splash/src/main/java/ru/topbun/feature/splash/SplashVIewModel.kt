package ru.topbun.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.topbun.domain.useCases.config.GetStatusFirstStartUseCase
import ru.topbun.domain.useCases.config.SetStatusFirstStartUseCase

class SplashVIewModel(
    private val getStatusFirstStartUseCase: GetStatusFirstStartUseCase,
    private val setStatusFirstStartUseCase: SetStatusFirstStartUseCase
): ViewModel() {

    init {
        handleFirstStart()
    }

    private fun handleFirstStart() = viewModelScope.launch {
        val status = getStatusFirstStartUseCase()
        if (status){
            setStatusFirstStartUseCase(false)
            TODO("Navigate to Auth")
        } else {
            TODO("Navigate to TabsScreen")
        }
    }

}