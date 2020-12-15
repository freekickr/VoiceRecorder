package com.freekickr.logic.domain

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.freekickr.core.App
import com.freekickr.core.di.ApplicationProvider
import com.freekickr.core.tools.Toaster
import com.freekickr.logic.MainActivity
import com.freekickr.logic.R
import com.freekickr.logic.dagger.di.AppActivityComponent
import com.freekickr.logic.database.daos.RecordDao
import com.freekickr.logic.database.entities.RecordItem
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RecordService : Service() {

    private var mFileName: String? = null
    private var mFilePath: String? = null

    private var mRecorder: MediaRecorder? = null

    private var mStartingTimeMillis: Long = 0
    private var mElapsedMillis: Long = 0

    @Inject
    lateinit var mDatabase: RecordDao

    @Inject
    lateinit var toaster: Toaster

    private val mJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mJob)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        inject(getApplicationProvider(applicationContext))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startRecording()
        return START_NOT_STICKY
    }

    private fun startRecording() {
        setFilenameAndPath()

        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder?.setOutputFile(mFilePath)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder?.setAudioChannels(1)
        mRecorder?.setAudioEncodingBitRate(192000)

        try {
            mRecorder?.prepare()
            mRecorder?.start()
            mStartingTimeMillis = System.currentTimeMillis()
            startForeground(1, createNotification())
        } catch (e: IOException) {
            Log.e("RecordService", "prepare failed")
        }
    }

    private fun createNotification(): Notification? {
        val builder =
            NotificationCompat.Builder(applicationContext, getString(R.string.notif_channel_id))
                .setSmallIcon(R.drawable.ic_mic_white_36)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notif_recording))
                .setOngoing(true)
        builder.setContentIntent(
            PendingIntent.getActivities(
                applicationContext, 0, arrayOf(
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                ), 0
            )
        )
        return builder.build()
    }

    private fun setFilenameAndPath() {
        var count = 0
        var file: File
        val dateTime = SimpleDateFormat(
            "yyyy_MM_dd_HH_mm_ss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())

        do {
            mFileName = ("${getString(R.string.default_filename)}_$dateTime$count.mp4")
            mFilePath = application.getExternalFilesDir(null)?.absolutePath
            mFilePath += "/$mFileName"

            count++

            file = File(mFilePath)
        } while (file.exists() && !file.isDirectory)
    }

    private fun stopRecording() {
        val recordingItem = RecordItem()

        mRecorder?.stop()
        mElapsedMillis = System.currentTimeMillis() - mStartingTimeMillis
        mRecorder?.release()
        toaster.show(getString(R.string.record_saved))

        recordingItem.name = mFileName.toString()
        recordingItem.filePath = mFilePath.toString()
        recordingItem.length = mElapsedMillis
        recordingItem.time = System.currentTimeMillis()

        mRecorder = null

        try {
            mUiScope.launch {
                withContext(Dispatchers.IO) {
                    mDatabase.insert(recordingItem)
                }
            }
        } catch (e: Exception) {
            Log.e("RecordService", "exception", e)
        }
    }

    override fun onDestroy() {
        if (mRecorder != null) {
            stopRecording()
        }
        super.onDestroy()
    }

    private fun inject(applicationProvider: ApplicationProvider) {
        AppActivityComponent
            .Initializer
            .init(applicationProvider)
            .inject(this@RecordService)
    }

    private fun getApplicationProvider(applicationContext: Context) =
        (applicationContext as App).getAppProvider()
}