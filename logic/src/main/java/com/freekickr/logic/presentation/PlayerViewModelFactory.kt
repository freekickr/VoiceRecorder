package com.freekickr.logic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.freekickr.core.App
import com.freekickr.core.tools.Toaster
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.presentation.viewmodels.PlayerViewModel
import com.freekickr.logic.presentation.viewmodels.RecordViewModel
import com.freekickr.logic.presentation.viewmodels.RecordsListViewModel
import com.freekickr.logic.presentation.viewmodels.RemoveRecordDialogViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class PlayerViewModelFactory @Inject constructor(
    private val app: App,
    private var itemPath: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            PlayerViewModel::class.java -> PlayerViewModel(app, itemPath) as T
            else -> throw IllegalArgumentException("Can't find viewmodel for class $modelClass")
        }
    }
}