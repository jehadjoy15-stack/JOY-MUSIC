package com.joymusic.shared.ui.mobile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.joymusic.shared.db.MusicDatabase
import com.joymusic.shared.model.SongItem
import com.joymusic.shared.ui.components.ModalBottomSheet
import com.joymusic.shared.ui.menu.SongDetailsContent
import com.joymusic.shared.ui.playback.PlaybackUiState
import com.joymusic.shared.ui.theme.ComponentShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MobileSongDetailsSheet(
    state: PlaybackUiState,
    database: MusicDatabase,
    onDismiss: () -> Unit,
    song: SongItem? = state.currentSong,
) {
    ModalBottomSheet(onDismissRequest = onDismiss, shape = ComponentShapes.BottomSheetShape) {
        SongDetailsContent(
            song = song,
            database = database,
            playbackResource = state.currentPlaybackResource,
            durationMs = state.durationMs.takeIf { it > 0 && state.currentSong?.id == song?.id },
        )
    }
}
