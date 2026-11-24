package com.joymusic.shared.settings

import com.joymusic.shared.constants.DiscordPendingAuthSessionKey
import com.joymusic.shared.constants.LyricsOpenRouterOAuthKey
import com.joymusic.shared.constants.LyricsOpenRouterOAuthSessionKey

internal val secureSettingsKeys =
    setOf(
        DiscordPendingAuthSessionKey,
        "innerTubeCookie",
        "visitorData",
        "dataSyncId",
        "innerTubeAuthUser",
        "lastFmSession",
        "listenBrainzToken",
        "lyricsAiApiKey",
        "lyricsAiApiKeys",
        LyricsOpenRouterOAuthKey,
        LyricsOpenRouterOAuthSessionKey,
        "lyricsDeepLApiKey",
        "accountName",
        "accountEmail",
        "accountChannelHandle",
        "accountThumbnail",
        "discordAccessToken",
        "discordRefreshToken",
        "listenTogetherDesktopSession",
        "listenTogetherServerUrl",
        "listenTogetherCustomServers",
        "listenTogetherServerMetadata",
        "deviceConnectPairedDevices",
        "listenTogetherPairedRoomCode",
        "spotifySession",
    )
