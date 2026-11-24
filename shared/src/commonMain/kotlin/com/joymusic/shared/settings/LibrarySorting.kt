package com.joymusic.shared.settings

import com.joymusic.shared.model.SortDirection
import kotlinx.serialization.Serializable

@Serializable
data class LibrarySorting(
    val sort: LibraryContentSort,
    val direction: SortDirection,
)
