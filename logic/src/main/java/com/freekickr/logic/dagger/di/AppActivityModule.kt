package com.freekickr.logic.dagger.di

import com.freekickr.core.App
import com.freekickr.logic.dagger.actions.NavControllerProviderAction
import com.freekickr.logic.dagger.actions.NavControllerProviderActionImpl
import com.freekickr.logic.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class AppActivityModule {

    @Provides
    fun provideAppDatabaseDao(app: App) = AppDatabase.getInstance(app.getApplicationContext()).recordDao

    @Provides
    fun bindNavControllerProvider(): NavControllerProviderAction = NavControllerProviderActionImpl()

}