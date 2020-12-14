package com.freekickr.logic.dagger.actions

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.freekickr.logic.R
import javax.inject.Inject

class NavControllerProviderActionImpl @Inject constructor() : NavControllerProviderAction {

    override fun get(activity: AppCompatActivity) =
        (activity.supportFragmentManager.findFragmentById(
            R.id.navHostFragmentContainer
        ) as NavHostFragment).navController

}