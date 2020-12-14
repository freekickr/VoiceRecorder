package com.freekickr.logic.presentation.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.freekickr.core.App
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.logic.R
import com.freekickr.logic.dagger.di.AppActivityComponent
import com.freekickr.logic.database.AppDatabase
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.databinding.FragmentRecordsListBinding
import com.freekickr.logic.presentation.ViewModelFactory
import com.freekickr.logic.presentation.adapters.RecordsListAdapter
import com.freekickr.logic.presentation.viewmodels.RecordViewModel
import com.freekickr.logic.presentation.viewmodels.RecordsListViewModel
import javax.inject.Inject


class RecordsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var databaseDao: RecordDao

    private val viewModel: RecordsListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RecordsListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(getApplicationProvider((context.applicationContext)))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRecordsListBinding>(inflater, R.layout.fragment_records_list, container, false)

        binding.viewModel = viewModel
        val adapter = RecordsListAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.records.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        binding.lifecycleOwner = this

        return binding.root
    }


    private fun inject(applicationProvider: ApplicationProvider) {
        AppActivityComponent
            .Initializer
            .init(applicationProvider)
            .inject(this@RecordsListFragment)
    }

    private fun getApplicationProvider(applicationContext: Context) =
        (applicationContext as App).getAppProvider()

}