package com.example.workmatetest.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmatetest.domain.usecase.SyncRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.workmatetest.domain.model.Result
import javax.inject.Inject

@HiltViewModel
class SplashReducer @Inject constructor(
    private val syncRecipesUseCase: SyncRecipesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SplashState(isLoading = true))
    val state: StateFlow<SplashState> = _state

    fun processIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.SyncData -> {
                viewModelScope.launch {
                    try {
                        when (val result = syncRecipesUseCase()) {
                            is Result.Success -> {
                                _state.value = _state.value.copy(isLoading = false)
                            }
                            is Result.Error -> {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message
                                )
                            }
                        }
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = "Неизвестная ошибка: ${e.message}"
                        )
                    }
                }
            }
        }
    }
}