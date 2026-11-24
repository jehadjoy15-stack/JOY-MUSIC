package com.joymusic.desktop.spotifyimport

import androidx.compose.runtime.Composable
import com.joymusic.shared.generated.resources.Res
import com.joymusic.shared.generated.resources.apple_music_session_sign_in
import com.joymusic.shared.generated.resources.apple_music_sign_in
import com.joymusic.shared.playlistimport.AppleMusicPlaylistImporter
import org.jetbrains.compose.resources.stringResource

@Composable
@Suppress("FunctionName")
internal fun AppleMusicLoginDialog(
    onDismissRequest: () -> Unit,
    onSessionToken: (String) -> Unit,
) {
    ProviderLoginDialog(
        title = stringResource(Res.string.apple_music_sign_in),
        loginUrl = APPLE_MUSIC_URL,
        signInHint = stringResource(Res.string.apple_music_session_sign_in),
        onDismissRequest = onDismissRequest,
        extractCredentials = { state ->
            val cookies = state.cookieManager.getCookies(APPLE_MUSIC_URL)
            val cookieHeader = cookies.joinToString("; ") { "${it.name}=${it.value}" }
            AppleMusicPlaylistImporter.extractAppleMusicUserToken(cookieHeader)
        },
        onAuthenticated = onSessionToken,
    )
}

private const val APPLE_MUSIC_URL = "https://music.apple.com"
