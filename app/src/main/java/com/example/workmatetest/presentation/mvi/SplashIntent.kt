package com.example.workmatetest.presentation.mvi

sealed class SplashIntent {
    object SyncData : SplashIntent()
}