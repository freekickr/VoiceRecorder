package com.freekickr.logic.presentation.viewmodels

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.freekickr.core.App
import com.freekickr.core.tools.Toaster
import com.freekickr.logic.R
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.database.entities.RecordItem
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import kotlin.properties.Delegates

class EditRecordDialogViewModel @Inject constructor(
    private val app: App,
    private val databaseDao: RecordDao,
    private val toaster: Toaster
) : ViewModel() {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var itemId by Delegates.notNull<Long>()

    val fileName = ObservableField("")
    private lateinit var record: RecordItem

    init {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                databaseDao.getRecord(itemId)?.let {
                    record = it
                    fileName.set(File(it.name).nameWithoutExtension)
                }
            }
        }
    }

    fun initData(id: Long) {
        itemId = id
    }

    fun editItem() {
        fileName.get()?.let { newName ->
            val newPath = renameFile(newName)
            newPath?.let {
                updateEntry(it)
            }
        }
    }

    private fun updateEntry(newPath: String) {
        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    databaseDao.update(
                        RecordItem(
                            record.id,
                            File(newPath).name,
                            newPath,
                            record.length,
                            record.time
                        )
                    )
                }
            }
            toaster.show(app.getApplicationContext().getString(R.string.file_renamed))
        } catch (e: Exception) {
            Log.e("EditItem", "exception", e)
        }
    }

    private fun renameFile(newName: String): String? {
        val file = File(record.filePath)
        val directory = file.parent
        val extension = file.extension
        val destinationFile = File(directory, "$newName.$extension")
        if (file.exists()) {
            file.renameTo(destinationFile)
            if (destinationFile.exists())
                return destinationFile.absolutePath
        }
        return null
    }

}