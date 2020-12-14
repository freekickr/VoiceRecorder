package com.freekickr.logic.dagger.actions

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

interface NavControllerProviderAction {
    fun get(activity: AppCompatActivity): NavController
}