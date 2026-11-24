package com.joymusic.shared.youtube.di

import com.metrolist.innertubex.InnerTube
import com.metrolist.innertubex.cipher.RemotePlayerConfigStore
import com.metrolist.innertubex.cipher.YouTubeCipherService
import com.metrolist.innertubex.extraction.InnerTubeExtractor
import com.metrolist.innertubex.extraction.StreamExtractor
import com.metrolist.innertubex.extraction.TokenProvider
import com.metrolist.innertubex.extraction.YtConfigParser
import com.metrolist.innertubex.extraction.YtConfigParserImpl
import com.metrolist.innertubex.extraction.strategy.ClientFallbackStrategy
import com.metrolist.innertubex.extraction.strategy.ClientHealthMonitor
import com.metrolist.innertubex.extraction.strategy.ContentAwareFallbackStrategy
import com.joymusic.shared.BuildConfig
import com.joymusic.shared.api.lyrics.AiModelCatalog
import com.joymusic.shared.api.lyrics.LyprioLyricsProvider
import com.joymusic.shared.api.lyrics.LyricsClient
import com.joymusic.shared.api.lyrics.LyricsFetcher
import com.joymusic.shared.api.lyrics.LyricsTranslationClient
import com.joymusic.shared.api.lyrics.YouTubeLyricsApi
import com.joymusic.shared.api.musicvideo.MusicVideoResolver
import com.joymusic.shared.repository.LyricsRepository
import com.joymusic.shared.state.GlobalYouTube
import com.joymusic.shared.youtube.logging.JoyMusicInnerTubeLogger
import com.joymusic.shared.youtube.lyrics.InnerTubeLyricsApi
import com.joymusic.shared.youtube.playback.DefaultStreamResolver
import com.joymusic.shared.youtube.playback.LocalAudioSource
import com.joymusic.shared.youtube.playback.JoyMusicPlayerConfigRepository
import com.joymusic.shared.youtube.playback.PlatformTokenProvider
import com.joymusic.shared.youtube.playback.StreamResolver
import com.joymusic.shared.youtube.strategy.ClientHealthTracker
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule =
    module {
        single {
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        }
        single { RemotePlayerConfigStore(get(), JoyMusicPlayerConfigRepository, JoyMusicInnerTubeLogger) }
        single<YtConfigParser> {
            YtConfigParserImpl(
                get(),
                get(),
                get(),
                JoyMusicInnerTubeLogger,
                cipherService = get<YouTubeCipherService>(),
            )
        }

        single { ClientHealthTracker() }
        single<ClientHealthMonitor> { get<ClientHealthTracker>() }
        single<ClientFallbackStrategy> { ContentAwareFallbackStrategy(get()) }

        single { YouTubeCipherService(get(), get(), JoyMusicInnerTubeLogger) }

        single<TokenProvider> { PlatformTokenProvider() }

        single<StreamExtractor> {
            InnerTubeExtractor(
                configParser = get(),
                cipherService = get<YouTubeCipherService>(),
                innerTube = get<InnerTube>(),
                fallbackStrategy = get(),
                tokenProvider = get<TokenProvider>(),
                clientHealthMonitor = get(),
                logger = JoyMusicInnerTubeLogger,
            )
        }

        single<StreamResolver> {
            DefaultStreamResolver(
                extractor = get(),
                innerTube = get(),
                tokenProvider = getOrNull(),
                clientHealthTracker = get(),
                playerConfigRefresh = { get<YouTubeCipherService>().refreshAfterStreamRejection() },
                musicVideoResolver = get(),
                localAudioSource = getOrNull<LocalAudioSource>(),
            )
        }

        single<MusicVideoResolver> { MusicVideoResolver() }

        single { GlobalYouTube.repository }
        single<YouTubeLyricsApi> { InnerTubeLyricsApi(get()) }
        single {
            LyricsClient(
                httpClient = get(),
                youtubeLyricsApi = get(),
                lyprioLyricsProvider =
                    LyprioLyricsProvider.createOrNull(
                        httpClient = get(),
                        baseUrl = BuildConfig.LYPRIO_BASE_URL,
                        signingKeyBase64 = BuildConfig.LYPRIO_HMAC_KEY,
                        allowInsecureHttp = BuildConfig.DEBUG,
                    ),
            )
        }
        single<LyricsFetcher> { get<LyricsClient>() }
        single { LyricsTranslationClient(get(), get()) }
        single { AiModelCatalog(get()) }
        single {
            LyricsRepository(
                lyricsClient = get(),
                lyricsTranslationClient = get(),
                aiModelCatalog = get(),
                databaseDao = getOrNull(),
                youtubeLyricsApi = get(),
            )
        }
    }
