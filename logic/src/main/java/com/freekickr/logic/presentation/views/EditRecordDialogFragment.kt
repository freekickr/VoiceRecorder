package com.freekickr.logic.presentation.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.freekickr.core.App
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.logic.R
import com.freekickr.logic.dagger.di.AppActivityComponent
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.databinding.FragmentEditRecordDialogBinding
import com.freekickr.logic.presentation.EditRecordViewModelFactory
import com.freekickr.logic.presentation.PlayerViewModelFactory
import com.freekickr.logic.presentation.ViewModelFactory
import com.freekickr.logic.presentation.viewmodels.EditRecordDialogViewModel
import com.freekickr.logic.presentation.viewmodels.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_edit_record_dialog.*
import kotlinx.android.synthetic.main.fragment_player.*
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class EditRecordDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_ITEM_ID = "recording_item_id"

        fun newInstance(id: Long): EditRecordDialogFragment {
            val fragment = EditRecordDialogFragment()
            val bundle = Bundle()
            bundle.putLong(ARG_ITEM_ID, id)

            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var app: App

    @Inject
    lateinit var databaseDao: RecordDao

    private lateinit var viewModel: EditRecordDialogViewModel

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    private val viewModel: EditRecordDialogViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory).get(EditRecordDialogViewModel::class.java)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(getApplicationProvider((context.applicationContext)))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val itemId = arguments?.getLong(ARG_ITEM_ID)

        itemId?.let {
            EditRecordViewModelFactory(app, itemId, databaseDao)
        }?.let {factory ->
            viewModel = ViewModelProvider(this, factory).get(EditRecordDialogViewModel::class.java)
        }

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_edit_record_dialog, null)
        val binding = DataBindingUtil.bind<FragmentEditRecordDialogBinding>(view)
        binding?.viewModel = viewModel

        val builder = AlertDialog.Builder(activity)
        builder.setView(binding?.root)

        return builder
            .setCancelable(false)
            .setPositiveButton(getString(R.string.dialog_ok)) { dialog, _ ->
                Toast.makeText(requireContext(), viewModel.fileName.get(), Toast.LENGTH_SHORT).show()
                try {
                    itemId?.let { viewModel.editItem() }
                } catch (e: Exception) {
                    Log.e("EditRecord", "exception", e)
                }
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

    private fun inject(applicationProvider: ApplicationProvider) {
        AppActivityComponent
            .Initializer
            .init(applicationProvider)
            .inject(this@EditRecordDialogFragment)
    }

    private fun getApplicationProvider(applicationContext: Context) =
        (applicationContext as App).getAppProvider()
}