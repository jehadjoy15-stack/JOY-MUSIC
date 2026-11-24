package com.joymusic.shared.settings

import com.joymusic.shared.model.CollectionSort
import com.joymusic.shared.model.PlaylistSongSort
import com.joymusic.shared.model.SortDirection
import kotlinx.serialization.Serializable

@Serializable
data class SavedPlaylistSongSorting(
    val sort: PlaylistSongSort,
    val direction: SortDirection,
)

@Serializable
data class SavedPlaylistCollectionSorting(
    val sort: CollectionSort,
    val direction: SortDirection,
)
