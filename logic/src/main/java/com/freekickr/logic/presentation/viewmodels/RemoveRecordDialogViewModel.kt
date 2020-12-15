package com.freekickr.logic.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.freekickr.core.App
import com.freekickr.core.tools.Toaster
import com.freekickr.logic.R
import com.freekickr.logic.database.daos.RecordDao
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class RemoveRecordDialogViewModel @Inject constructor(
    private val app: App,
    private val databaseDao: RecordDao,
    private val toaster: Toaster
) : ViewModel() {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun removeItem(itemId: Long) {
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    databaseDao.removeRecord(itemId)
                }
            }
        } catch (e: Exception) {
            Log.e("RemoveItem", "exception", e)
        }
    }


    fun removeFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
            toaster.show(app.getApplication().getString(R.string.file_deleted))
        }
    }
}