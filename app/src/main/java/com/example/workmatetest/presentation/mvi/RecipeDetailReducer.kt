package com.example.workmatetest.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmatetest.BuildConfig
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.local.entity.RecipeEntity
import com.example.workmatetest.data.remote.SpoonacularApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailReducer @Inject constructor(
    private val userDao: UserDao,
    private val apiService: SpoonacularApiService
) : ViewModel() {
    private val _state = MutableStateFlow(RecipeDetailState())
    val state: StateFlow<RecipeDetailState> = _state

    fun processIntent(intent: RecipeDetailIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            var recipe = userDao.getRecipeById(intent.recipeId)?.toDomainRecipe()
            if (recipe == null) {
                try {
                    val response = apiService.getRecipeById(intent.recipeId, BuildConfig.SPOONACULAR_API_KEY)
                    if (response.isSuccessful) {
                        val entityFromApi = response.body()
                        if (entityFromApi != null) {

                            val recipeEntity = RecipeEntity(
                                id = entityFromApi.id,
                                title = entityFromApi.title,
                                image = entityFromApi.image,
                                summary = entityFromApi.summary
                            )
                            userDao.insertRecipe(recipeEntity)
                            recipe = recipeEntity.toDomainRecipe()
                        } else {
                            _state.value = _state.value.copy(errorMessage = "Рецепт не найден")
                        }
                    } else {
                        _state.value = _state.value.copy(errorMessage = response.message())
                    }
                } catch (e: Exception) {
                    _state.value = _state.value.copy(errorMessage = e.message)
                }
            }
            _state.value = _state.value.copy(isLoading = false, recipe = recipe)
        }
    }
}
