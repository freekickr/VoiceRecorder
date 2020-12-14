package com.freekickr.logic.dagger.actions

import android.content.Context
import android.content.Intent
import com.freekickr.core.actions.ShowAppActivityAction
import com.freekickr.logic.MainActivity
import javax.inject.Inject

class ShowAppActivityActionImpl @Inject constructor(): ShowAppActivityAction {
    override fun show(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}