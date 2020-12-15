package com.freekickr.logic.dagger.di

import com.freekickr.core.di.ApplicationProvider
import com.freekickr.logic.MainActivity
import com.freekickr.logic.presentation.views.EditRecordDialogFragment
import com.freekickr.logic.domain.RecordService
import com.freekickr.logic.presentation.views.PlayerFragment
import com.freekickr.logic.presentation.views.RecordFragment
import com.freekickr.logic.presentation.views.RecordsListFragment
import com.freekickr.logic.presentation.views.RemoveRecordDialogFragment
import dagger.Component

@Component(
    dependencies = [ApplicationProvider::class],
    modules = [AppActivityModule::class]
)
interface AppActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(service: RecordService)
    fun inject(fragment: RecordFragment)
    fun inject(fragment: RecordsListFragment)
    fun inject(fragment: PlayerFragment)
    fun inject(fragment: RemoveRecordDialogFragment)
    fun inject(fragment: EditRecordDialogFragment)

    class Initializer private constructor() {
        companion object {
            fun init(appProvider: ApplicationProvider): AppActivityComponent =
                DaggerAppActivityComponent.builder()
                    .applicationProvider(appProvider)
                    .build()
        }
    }
}