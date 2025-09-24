package com.example.workmatetest.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.local.db.UserDatabase
import com.example.workmatetest.data.paging.RecipeRemoteMediator
import com.example.workmatetest.data.remote.SpoonacularApiService
import com.example.workmatetest.domain.model.Recipe
import com.example.workmatetest.domain.usecase.SyncRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class RecipesReducer @Inject constructor(
    private val syncRecipesUseCase: SyncRecipesUseCase,
    private val userDao: UserDao,
    private val apiService: SpoonacularApiService,
    private val database: UserDatabase
) : ViewModel() {
    private val _state = MutableStateFlow(RecipesState())
    val state: StateFlow<RecipesState> = _state

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val recipesFlow: Flow<PagingData<Recipe>> = _searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = RecipeRemoteMediator(apiService, database),
            pagingSourceFactory = { userDao.getAllRecipesPaging() }
        ).flow
            .map { pagingData -> pagingData.map { it.toDomainRecipe() } }
            .map { pagingData ->
                if (query.isNotEmpty()) {
                    pagingData.filter { it.title.contains(query, ignoreCase = true) }
                } else {
                    pagingData
                }
            }
            .cachedIn(viewModelScope)
    }

    fun processIntent(intent: RecipesIntent) {
        when (intent) {
            is RecipesIntent.LoadRecipes -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isLoading = true)
                    syncRecipesUseCase()
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
            is RecipesIntent.SearchRecipes -> {
                _searchQuery.value = intent.query
            }
            is RecipesIntent.SelectRecipe -> {
                _state.value = _state.value.copy(selectedRecipe = intent.recipe)
            }
            is RecipesIntent.LoadUserName -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val user = userDao.getUser()
                    _state.value = _state.value.copy(userName = user?.name ?: "")
                }
            }
        }
    }
}