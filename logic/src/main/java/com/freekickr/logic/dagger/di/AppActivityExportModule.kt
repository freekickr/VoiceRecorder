package com.freekickr.logic.dagger.di

import com.freekickr.core.actions.ShowAppActivityAction
import com.freekickr.logic.dagger.actions.ShowAppActivityActionImpl
import dagger.Binds
import dagger.Module

@Module
interface AppActivityExportModule {
    @Binds
    fun bindShowAppActivityAction(showAppActivityActionImpl: ShowAppActivityActionImpl): ShowAppActivityAction
}