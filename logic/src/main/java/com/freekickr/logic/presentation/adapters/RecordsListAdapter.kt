package com.freekickr.logic.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.freekickr.logic.R
import com.freekickr.logic.database.entities.RecordItem
import com.freekickr.logic.presentation.views.EditRecordDialogFragment
import com.freekickr.logic.presentation.views.PlayerFragment
import com.freekickr.logic.presentation.views.RemoveRecordDialogFragment
import kotlinx.android.synthetic.main.item_records_list.view.*
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

class RecordsListAdapter : RecyclerView.Adapter<RecordsViewHolder>() {

    var data = listOf<RecordItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_records_list, parent, false)
        return RecordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}

class RecordsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(record: RecordItem) {
        val itemDuration = record.length
        val minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes)

        view.tvFileName.text = record.name
        view.tvFileLength.text = String.format("%02d:%02d", minutes, seconds)

        view.itemPlayLayout.setOnClickListener {
            val filePath = record.filePath
            val file = File(filePath)
            if (file.exists()) {
                try {
                    onPlayRecord(filePath, view.context)
                } catch (e: Exception) {
                }
            } else {
                Toast.makeText(view.context, R.string.file_doesnt_exist, Toast.LENGTH_SHORT).show()
            }
        }

        view.imageViewDelete.setOnClickListener {
            removeItemDialog(record, view.context)
        }

        view.imageViewEdit.setOnClickListener {
            editItemDialog(record, view.context)
        }
    }

    private fun onPlayRecord(filePath: String, context: Context?) {
        val playerFragment = PlayerFragment.newInstance(filePath)
        val transaction: FragmentTransaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        playerFragment.show(transaction, "dialog_playback")
    }

    private fun removeItemDialog(
        item: RecordItem,
        context: Context?
    ) {
        val removeDialogFragment = RemoveRecordDialogFragment.newInstance(item.id, item.filePath)
        val transaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        removeDialogFragment.show(transaction, "dialog_remove")
    }

    private fun editItemDialog(
        item: RecordItem,
        context: Context?
    ) {
        val editDialogFragment = EditRecordDialogFragment.newInstance(item.id)
        val transaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        editDialogFragment.show(transaction, "dialog_edit")
    }
}