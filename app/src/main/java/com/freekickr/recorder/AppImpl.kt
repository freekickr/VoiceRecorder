package com.freekickr.recorder

import android.app.Application
import com.freekickr.core.App
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.recorder.di.AppComponent

class AppImpl: Application(), App {

    private val appComponent: AppComponent by lazy { AppComponent.Initializer.init(this@AppImpl) }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun getAppProvider(): ApplicationProvider = appComponent

    override fun getApplication() = this as Application
}