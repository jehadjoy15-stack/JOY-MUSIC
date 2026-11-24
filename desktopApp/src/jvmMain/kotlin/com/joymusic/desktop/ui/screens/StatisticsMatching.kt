package com.joymusic.desktop.ui.screens

import com.joymusic.desktop.history.AlbumStat
import com.joymusic.shared.model.AlbumItem
import com.joymusic.shared.ui.statistics.matchingStatisticsAlbum

internal fun statisticsArtistName(id: String): String = id.removePrefix("artist:").substringAfter(':')

internal fun List<AlbumItem>.matchingStatisticsAlbum(album: AlbumStat): AlbumItem? = matchingStatisticsAlbum(album.name, album.artists)
