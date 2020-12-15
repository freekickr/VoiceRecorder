package com.freekickr.logic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.freekickr.core.App
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.presentation.viewmodels.EditRecordDialogViewModel
import com.freekickr.logic.presentation.viewmodels.PlayerViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class EditRecordViewModelFactory @Inject constructor(
    private val app: App,
    private var itemId: Long,
    private val databaseDao: RecordDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            EditRecordDialogViewModel::class.java -> EditRecordDialogViewModel(app, itemId, databaseDao) as T
            else -> throw IllegalArgumentException("Can't find viewmodel for class $modelClass")
        }
    }
}