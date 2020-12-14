package com.freekickr.logic

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ui.NavigationUI
import com.freekickr.core.App
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.logic.dagger.actions.NavControllerProviderAction
import com.freekickr.logic.dagger.di.AppActivityComponent
import com.freekickr.logic.domain.RecordService
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navControllerProvider: NavControllerProviderAction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inject(getApplicationProvider(this.applicationContext))
        NavigationUI.setupWithNavController(bottomNavigation, navControllerProvider.get(this))
    }

    fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("com.freekickr.logic.domain.RecordService" == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun inject(applicationProvider: ApplicationProvider) {
        AppActivityComponent
            .Initializer
            .init(applicationProvider)
            .inject(this@MainActivity)
    }

    private fun getApplicationProvider(applicationContext: Context) =
        (applicationContext as App).getAppProvider()
}