package com.joymusic.shared.utils

import kotlin.time.Clock

object JoyMusicClock {
    fun nowMs(): Long = Clock.System.now().toEpochMilliseconds()
}
