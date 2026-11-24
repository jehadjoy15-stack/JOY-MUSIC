package com.joymusic.shared.state

import com.joymusic.shared.db.entities.isEpisodeComplete
import com.joymusic.shared.model.SongItem
import com.joymusic.shared.model.isPodcast

fun podcastResumePosition(
    song: SongItem,
    savedPositionMs: Long?,
    durationMs: Long = song.duration?.times(1000L) ?: 0L,
    requestedPositionMs: Long? = null,
): Long {
    requestedPositionMs?.let { return it.coerceAtLeast(0L) }
    if (!song.isPodcast) return 0L
    val position =
        savedPositionMs?.takeIf { it >= 0L }
            ?: song.playbackPositionMs?.takeIf { it > 0L }
            ?: song.playbackProgressPercent?.takeIf { it in 0..100 && durationMs > 0L }?.let { durationMs * it / 100L }
            ?: 0L
    return if (isEpisodeComplete(position, durationMs)) 0L else position
}
