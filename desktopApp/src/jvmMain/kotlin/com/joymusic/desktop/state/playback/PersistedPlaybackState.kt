package com.joymusic.desktop.state.playback

import com.joymusic.shared.model.RepeatMode
import com.joymusic.shared.playback.MusicQueueSnapshot
import com.joymusic.shared.playback.StoredQueueSnapshotSerializer
import kotlinx.serialization.Serializable

@Serializable
data class PersistedPlaybackState(
    @Serializable(with = StoredQueueSnapshotSerializer::class)
    val queue: MusicQueueSnapshot,
    val positionMs: Long = 0L,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val revision: String? = null,
)
