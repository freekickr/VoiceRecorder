package com.freekickr.logic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.freekickr.core.App
import com.freekickr.core.tools.Toaster
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.presentation.viewmodels.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val app: App,
    private val databaseDao: RecordDao,
    private val toaster: Toaster
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            RecordViewModel::class.java -> RecordViewModel(app) as T
            RecordsListViewModel::class.java -> RecordsListViewModel(databaseDao) as T
            RemoveRecordDialogViewModel::class.java -> RemoveRecordDialogViewModel(app, databaseDao, toaster) as T
            EditRecordDialogViewModel::class.java -> EditRecordDialogViewModel(app, databaseDao, toaster) as T
            PlayerViewModel::class.java -> PlayerViewModel(app) as T
            else -> throw IllegalArgumentException("Can't find viewmodel for class $modelClass")
        }
    }
}