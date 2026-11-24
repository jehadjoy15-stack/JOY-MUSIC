package com.joymusic.shared.db

import com.joymusic.shared.db.entities.AlbumEntity
import com.joymusic.shared.db.entities.Song
import com.joymusic.shared.db.entities.SongEntity
import com.joymusic.shared.db.entities.SpeedDialItem
import com.joymusic.shared.db.entities.toSongItem
import com.joymusic.shared.model.SongItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SongArtworkFallbackTest {
    @Test
    fun storedAlbumCoverReachesQueueItemsAndSpeedDialWithoutReplacingTrackArtwork() {
        val album = AlbumEntity("album", title = "Album", thumbnailUrl = "album-cover", songCount = 2, duration = 400)
        for (thumbnail in listOf(null, "", "track-cover")) {
            val song =
                Song(
                    song = SongEntity("song", "Song", thumbnailUrl = thumbnail, albumId = album.id, albumName = album.title),
                    artists = emptyList(),
                    album = album,
                )
            val expected = thumbnail?.takeIf(String::isNotBlank) ?: album.thumbnailUrl
            assertEquals(expected, song.thumbnailUrl)
            assertEquals(expected, song.toSongItem().thumbnail)
            val pin = SpeedDialItem.fromYTItem(song.toSongItem())
            assertEquals(expected, (pin.toYTItem() as SongItem).thumbnail)
        }
        assertNull(Song(SongEntity("unknown", "Song"), emptyList()).toSongItem().thumbnail)
    }
}
