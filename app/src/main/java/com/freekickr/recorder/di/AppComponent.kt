package com.freekickr.recorder.di

import com.freekickr.core.di.AppActivityProvider
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.core.di.MainToolsProvider
import com.freekickr.logic.dagger.di.AppActivityExportComponent
import com.freekickr.recorder.AppImpl
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [MainToolsProvider::class,
    AppActivityProvider::class]
)
@Singleton
interface AppComponent: ApplicationProvider {
    fun inject(app: AppImpl)
    class Initializer private constructor() {
        companion object {
            fun init(app: AppImpl): AppComponent {
                val mainToolsProvider = MainToolsComponent.Initializer.init(app)
                val appActivityProvider = AppActivityExportComponent.Initializer.init(mainToolsProvider)
                return DaggerAppComponent.builder()
                    .mainToolsProvider(mainToolsProvider)
                    .appActivityProvider(appActivityProvider)
                    .build()
            }
        }
    }
}