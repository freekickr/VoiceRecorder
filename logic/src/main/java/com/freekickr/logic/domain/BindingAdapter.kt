package com.freekickr.logic.domain

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter(value = ["android:setTextWatcher"])
    @JvmStatic
    fun bindEditTextChangesListener(target: EditText, watcher: TextWatcher) {
        target.addTextChangedListener(watcher)
    }

}