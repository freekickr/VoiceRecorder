package com.freekickr.logic.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.*
import com.freekickr.core.App
import com.freekickr.logic.database.daos.RecordDao
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class PlayerViewModel(app: App) : AndroidViewModel(app.getApplication()),
    LifecycleObserver {
    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?>
        get() = _player
    private var contentPosition = 0L
    private var playWhenReady = true
    private lateinit var itemPath: String

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegrounded() {
        setupPlayer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgrounded() {
        releaseExoPlayer()
    }

    fun initData(path: String) {
        itemPath = path
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupPlayer() {
        val dataSourceFactory = DefaultDataSourceFactory(
            getApplication(),
            Util.getUserAgent(getApplication(), "recorder")
        )

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory, DefaultExtractorsFactory())
            .createMediaSource(MediaItem.fromUri(Uri.parse(itemPath)))

        val player = SimpleExoPlayer.Builder(getApplication()).build()
        player.prepare(mediaSource)
        player.playWhenReady = playWhenReady
        player.seekTo(contentPosition)

        this._player.value = player
    }

    private fun releaseExoPlayer() {
        val player = _player.value ?: return
        this._player.value = null
        contentPosition = player.contentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
        releaseExoPlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}