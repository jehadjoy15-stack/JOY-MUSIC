package com.joymusic.shared.playback

import com.joymusic.shared.model.SongItem

data class QueuePlaybackRequest(
    val requestId: Long,
    val song: SongItem,
    val positionMs: Long = 0L,
    val resumePlayback: Boolean = true,
)
