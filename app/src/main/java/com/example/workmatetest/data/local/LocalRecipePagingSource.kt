package com.example.workmatetest.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.local.entity.RecipeEntity

class LocalRecipePagingSource(
    private val userDao: UserDao
) : PagingSource<Int, RecipeEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeEntity> {
        val offset = params.key ?: 0
        val pageSize = params.loadSize

        return try {
            val recipes = userDao.getAllRecipes()
            LoadResult.Page(
                data = recipes,
                prevKey = if (offset == 0) null else offset - pageSize,
                nextKey = if (recipes.size < pageSize) null else offset + pageSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize) ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }
}