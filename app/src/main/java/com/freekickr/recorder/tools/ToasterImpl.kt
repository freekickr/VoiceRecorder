package com.freekickr.recorder.tools

import android.content.Context
import android.widget.Toast
import com.freekickr.core.tools.Toaster
import javax.inject.Inject

class ToasterImpl @Inject constructor(
    private val context: Context
): Toaster {
    override fun show(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}