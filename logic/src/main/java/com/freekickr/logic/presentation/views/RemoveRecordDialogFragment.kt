package com.freekickr.logic.presentation.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.freekickr.core.App
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.logic.R
import com.freekickr.logic.dagger.di.AppActivityComponent
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.presentation.ViewModelFactory
import com.freekickr.logic.presentation.viewmodels.PlayerViewModel
import com.freekickr.logic.presentation.viewmodels.RemoveRecordDialogViewModel
import java.lang.Exception
import javax.inject.Inject

class RemoveRecordDialogFragment: DialogFragment() {

    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"
        private const val ARG_ITEM_ID = "recording_item_id"

        fun newInstance(itemId: Long, itemPath: String?): RemoveRecordDialogFragment {
            val fragment = RemoveRecordDialogFragment()
            val bundle = Bundle()
            bundle.putLong(ARG_ITEM_ID, itemId)
            bundle.putString(ARG_ITEM_PATH, itemPath)

            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: RemoveRecordDialogViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RemoveRecordDialogViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(getApplicationProvider((context.applicationContext)))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val itemPath = arguments?.getString(ARG_ITEM_PATH)
        val itemId = arguments?.getLong(ARG_ITEM_ID)

        return AlertDialog.Builder(activity)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_text)
            .setPositiveButton(R.string.dialog_yes) { dialog, _ ->
                try {
                    itemId?.let { viewModel.removeItem(it) }
                    itemPath?.let { viewModel.removeFile(it) }
                } catch (e: Exception) {
                    Log.e("RemoveRecord", "exception", e)
                }
                dialog.cancel()
            }
            .setNegativeButton(R.string.dialog_no) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

    private fun inject(applicationProvider: ApplicationProvider) {
        AppActivityComponent
            .Initializer
            .init(applicationProvider)
            .inject(this@RemoveRecordDialogFragment)
    }

    private fun getApplicationProvider(applicationContext: Context) =
        (applicationContext as App).getAppProvider()
}