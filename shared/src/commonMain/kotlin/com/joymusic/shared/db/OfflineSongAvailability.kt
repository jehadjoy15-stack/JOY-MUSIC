package com.joymusic.shared.db

fun interface OfflineSongAvailability {
    suspend fun songIds(): Set<String>
}
