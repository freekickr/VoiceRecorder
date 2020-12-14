package com.freekickr.core.di

import com.freekickr.core.App
import com.freekickr.core.actions.ShowAppActivityAction
import com.freekickr.core.tools.Toaster

interface ApplicationProvider: MainToolsProvider,
        AppActivityProvider

interface MainToolsProvider {
    fun provideContext(): App
    fun provideToast(): Toaster
}

interface AppActivityProvider {
    fun provideAppActivity(): ShowAppActivityAction
}