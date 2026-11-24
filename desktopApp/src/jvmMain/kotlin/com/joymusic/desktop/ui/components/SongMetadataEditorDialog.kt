package com.joymusic.desktop.ui.components

import androidx.compose.runtime.Composable
import com.joymusic.desktop.state.app.AppState
import com.joymusic.shared.db.MusicDatabase
import com.joymusic.shared.model.SongItem
import com.joymusic.shared.model.editSongMetadata
import com.joymusic.shared.model.resetSongMetadata
import com.joymusic.shared.state.SongMetadataState
import com.joymusic.shared.ui.menu.EditSongDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.koin.core.context.GlobalContext

@Composable
internal fun SongMetadataEditorDialog(
    song: SongItem,
    onSaved: () -> Unit,
    onDismiss: () -> Unit,
) {
    val effectiveSong = SongMetadataState.apply(song)
    EditSongDialog(
        title = effectiveSong.title,
        artist =
            effectiveSong.artists
                .firstOrNull()
                ?.name
                .orEmpty(),
        onSave = { title, artist ->
            AppState.scope
                .async(Dispatchers.IO) {
                    editSongMetadata(GlobalContext.get().get<MusicDatabase>(), song, title, artist)
                }.await()
        },
        onSaved = onSaved,
        onDismiss = onDismiss,
        onReset = {
            AppState.scope
                .async(Dispatchers.IO) {
                    resetSongMetadata(GlobalContext.get().get<MusicDatabase>(), song.id)
                }.await()
        },
    )
}
