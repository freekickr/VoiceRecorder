package com.freekickr.logic.dagger.di

import com.freekickr.core.di.AppActivityProvider
import com.freekickr.core.di.MainToolsProvider
import dagger.Component

@Component(
    dependencies = [MainToolsProvider::class],
    modules = [AppActivityExportModule::class]
)
interface AppActivityExportComponent: AppActivityProvider {
    class Initializer private constructor() {
        companion object {
            fun init(mainToolsProvider: MainToolsProvider): AppActivityProvider {
                return DaggerAppActivityExportComponent.builder()
                    .mainToolsProvider(mainToolsProvider)
                    .build()
            }
        }
    }

}