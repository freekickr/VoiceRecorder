package com.freekickr.core

import android.app.Application
import android.content.Context
import com.freekickr.core.di.ApplicationProvider

interface App {
    fun getApplicationContext(): Context
    fun getAppProvider(): ApplicationProvider
    fun getApplication(): Application
}