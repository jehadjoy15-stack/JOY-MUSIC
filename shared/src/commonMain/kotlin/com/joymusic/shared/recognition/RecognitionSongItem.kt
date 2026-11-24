package com.joymusic.shared.recognition

import com.joymusic.shared.model.AlbumTiny
import com.joymusic.shared.model.ArtistTiny
import com.joymusic.shared.model.SongItem

internal fun recognitionSongItem(
    youtubeVideoId: String?,
    title: String,
    artist: String,
    album: String?,
    coverArtUrl: String?,
    coverArtHqUrl: String?,
    genre: String?,
): SongItem? =
    youtubeVideoId?.takeIf { it.isNotBlank() }?.let { videoId ->
        SongItem(
            id = videoId,
            title = title,
            thumbnail = coverArtHqUrl ?: coverArtUrl,
            artists = listOf(ArtistTiny(id = null, name = artist)),
            album = album?.let { AlbumTiny(id = null, name = it) },
            genre = genre,
        )
    }
