package com.freekickr.logic.presentation.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.freekickr.core.App
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.logic.R
import com.freekickr.logic.dagger.di.AppActivityComponent
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.presentation.PlayerViewModelFactory
import com.freekickr.logic.presentation.viewmodels.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_player.*
import javax.inject.Inject

class PlayerFragment : DialogFragment() {

    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"

        fun newInstance(itemPath: String?): PlayerFragment {
            val fragment = PlayerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_ITEM_PATH, itemPath)

            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var app: App

    @Inject
    lateinit var databaseDao: RecordDao

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(getApplicationProvider((context.applicationContext)))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemPath = arguments?.getString(ARG_ITEM_PATH)
        playerView.showTimeoutMs = 0

        itemPath?.let {
            PlayerViewModelFactory(app, it)
        }?.let {factory ->
            val viewModel = ViewModelProvider(this, factory).get(PlayerViewModel::class.java)
            viewModel.player.observe(viewLifecycleOwner, Observer {
                playerView.player = it
            })
        }
    }

    private fun inject(applicationProvider: ApplicationProvider) {
        AppActivityComponent
            .Initializer
            .init(applicationProvider)
            .inject(this@PlayerFragment)
    }

    private fun getApplicationProvider(applicationContext: Context) =
        (applicationContext as App).getAppProvider()

}