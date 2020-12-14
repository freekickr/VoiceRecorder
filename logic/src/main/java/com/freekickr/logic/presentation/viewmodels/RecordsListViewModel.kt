package com.freekickr.logic.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.freekickr.logic.database.daos.RecordDao

class RecordsListViewModel(
    dataSource: RecordDao
): ViewModel() {

    val database = dataSource
    val records = database.getAllRecords()

}