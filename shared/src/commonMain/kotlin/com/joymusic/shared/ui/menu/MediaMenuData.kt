package com.joymusic.shared.ui.menu

import com.joymusic.shared.api.playlistSongs
import com.joymusic.shared.model.AlbumItem
import com.joymusic.shared.model.ArtistItem
import com.joymusic.shared.model.EpisodeItem
import com.joymusic.shared.model.PlaylistItem
import com.joymusic.shared.model.PodcastItem
import com.joymusic.shared.model.SongItem
import com.joymusic.shared.model.YTItem
import com.joymusic.shared.model.toSongItem
import com.joymusic.shared.state.GlobalYouTubeRepository

suspend fun loadMediaItemSongs(item: YTItem): List<SongItem> {
    val repository = GlobalYouTubeRepository.instance
    return when (item) {
        is SongItem -> {
            listOf(item)
        }

        is EpisodeItem -> {
            listOf(item.toSongItem())
        }

        is AlbumItem -> {
            repository.getAlbum(item.id)?.songs.orEmpty()
        }

        is ArtistItem -> {
            repository.getArtistSongs(item.id)
        }

        is PlaylistItem -> {
            repository.getPlaylist(item.id).playlistSongs()
        }

        is PodcastItem -> {
            // Every page, so playback covers the whole podcast and can start from its first episode.
            buildList {
                repository.getPodcastPages(item.id).collect { page -> page.episodes.mapTo(this) { it.toSongItem(page.podcast.title) } }
            }
        }

        else -> {
            emptyList()
        }
    }
}
