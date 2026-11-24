package com.joymusic.shared.playback

import com.joymusic.shared.youtube.playback.PlaybackResource

internal fun canUseIosAudioProcessingTap(
    resource: PlaybackResource?,
    url: String,
): Boolean {
    if (resource != null) return resource.isDirectFile || resource.isSabr
    val path = url.substringBefore('?').lowercase()
    return !path.endsWith(".m3u8") && !path.endsWith(".mpd")
}
