package com.joymusic.shared.api

class PlaylistNotFoundException(
    val playlistId: String,
) : Exception()
