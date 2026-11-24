package com.joymusic.shared

import com.joymusic.shared.api.lastfm.LastFM
import com.joymusic.shared.auth.AuthManager
import com.joymusic.shared.backup.IosBackupRestoreManager
import com.joymusic.shared.db.MusicDatabase
import com.joymusic.shared.db.daos.DatabaseDao
import com.joymusic.shared.db.getDatabaseBuilder
import com.joymusic.shared.discord.DiscordRpcManager
import com.joymusic.shared.playback.MusicPlayer
import com.joymusic.shared.repository.LyricsRepository
import com.joymusic.shared.scrobble.ScrobbleSessions
import com.joymusic.shared.settings.BlockedContent
import com.joymusic.shared.settings.Settings
import com.joymusic.shared.state.GlobalYouTube
import com.joymusic.shared.state.SongLikeState
import com.joymusic.shared.state.coreModule
import com.joymusic.shared.youtube.di.sharedModule
import com.joymusic.shared.youtube.playback.StreamResolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

object JoyMusicPlatform {
    private var isInitialized = false

    fun initialize() {
        if (isInitialized) return
        IosBackupRestoreManager().recoverInterruptedRestore()
        isInitialized = true
        startKoin {
            modules(
                coreModule,
                sharedModule,
                module {
                    single<MusicDatabase> { getDatabaseBuilder(null).build() }
                    single<DatabaseDao> { get<MusicDatabase>().dao() }
                    single { MusicPlayer() }
                },
            )
        }

        LastFM.initialize(
            apiKey = BuildConfig.LASTFM_API_KEY,
            secret = BuildConfig.LASTFM_SECRET,
        )
        LastFM.sessionKey = Settings.shared.lastFmSession.ifBlank { null }
        ScrobbleSessions.restorePersistedAccounts()

        AuthManager().restorePersistedSession()

        val startupScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        startupScope.launch {
            BlockedContent.refreshAiSlopBlocklist()
        }
        startupScope.launch {
            runCatching {
                KoinPlatform.getKoin().get<StreamResolver>().prewarm()
            }
        }
    }

    suspend fun reopenDatabaseAfterRestore(): MusicDatabase {
        val koin = KoinPlatform.getKoin()
        val database = getDatabaseBuilder(null).build()
        koin.declare(database)
        koin.declare(database.dao())
        koin.declare(MusicPlayer())
        koin.declare(
            LyricsRepository(
                lyricsClient = koin.get(),
                lyricsTranslationClient = koin.get(),
                databaseDao = database.dao(),
                youtubeLyricsApi = koin.get(),
                aiModelCatalog = koin.get(),
            ),
        )
        refreshRestoredSettings()
        return database
    }

    suspend fun refreshRestoredSettings() {
        DiscordRpcManager.reloadAfterRestore(GlobalYouTube.client)
        AuthManager().restorePersistedSession()
        ScrobbleSessions.restorePersistedAccounts()
        SongLikeState.refreshFromSettings()
    }
}
