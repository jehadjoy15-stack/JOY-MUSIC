/**
 * JoyMusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.joymusic.desktop.di

import com.joymusic.desktop.state.playback.PlayerState
import com.joymusic.desktop.storage.DownloadManager
import com.joymusic.desktop.storage.SongCacheManager
import com.joymusic.desktop.storage.desktopSongOfflineAvailability
import com.joymusic.desktop.youtube.DesktopPageBoundTokenRuntime
import com.metrolist.innertubex.extraction.TokenProvider
import com.joymusic.shared.db.MusicDatabase
import com.joymusic.shared.db.OfflineSongAvailability
import com.joymusic.shared.db.daos.DatabaseDao
import com.joymusic.shared.db.getDatabaseBuilder
import com.joymusic.shared.youtube.playback.DesktopTokenProvider
import com.joymusic.shared.youtube.playback.LocalAudioSource
import org.koin.dsl.module

val composeStateModule =
    module {
        single<MusicDatabase> { getDatabaseBuilder().build() }
        single<LocalAudioSource> {
            LocalAudioSource { id, song ->
                DownloadManager.getLocalPlaybackResource(id, song) ?: SongCacheManager.getCachedPlaybackResource(id, song)
            }
        }
        single<DatabaseDao> { get<MusicDatabase>().dao() }
        single<OfflineSongAvailability> { desktopSongOfflineAvailability(get()) }
        single { DesktopPageBoundTokenRuntime() }
        single<TokenProvider> {
            DesktopTokenProvider(get<DesktopPageBoundTokenRuntime>()) {
                val queue = PlayerState.queue.snapshots.value
                queue.displayedItems
                    .asSequence()
                    .drop(queue.currentIndex + 1)
                    .take(2)
                    .map { it.id }
                    .toList()
            }
        }
    }
