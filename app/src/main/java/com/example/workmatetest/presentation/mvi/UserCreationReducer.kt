package com.example.workmatetest.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmatetest.data.local.db.UserDatabase
import com.example.workmatetest.data.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCreationReducer @Inject constructor(
    private val db: UserDatabase
) : ViewModel() {
    private val _state = MutableStateFlow(UserCreationState())
    val state: StateFlow<UserCreationState> = _state

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun processIntent(intent: UserCreationIntent) {
        when (intent) {
            is UserCreationIntent.UpdateSurname -> {
                _state.value = _state.value.copy(surname = intent.surname)
            }
            is UserCreationIntent.UpdateName -> {
                _state.value = _state.value.copy(name = intent.name)
            }
            is UserCreationIntent.SaveUser -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                    val user = UserEntity(
                        surname = _state.value.surname,
                        name = _state.value.name,
                        favoriteRecipeIds = emptyList()
                    )
                    db.userDao().insertUser(user)
                    _state.value = _state.value.copy(isLoading = false)
                    _navigationEvent.value = "splash"
                }
            }
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}