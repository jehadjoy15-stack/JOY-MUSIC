/**
 * JoyMusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.joymusic.desktop.ui.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.joymusic.desktop.state.playback.PlayerState
import com.joymusic.shared.generated.resources.Res
import com.joymusic.shared.generated.resources.app_name
import com.joymusic.shared.generated.resources.logo
import com.joymusic.shared.ui.screens.AboutScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DesktopAboutScreen(
    colorScheme: ColorScheme,
    onDevtoolsUnlocked: () -> Unit = {},
) {
    AboutScreen(
        logo = painterResource(Res.drawable.logo),
        appName = stringResource(Res.string.app_name),
        colorScheme = colorScheme,
        contentPadding = PaddingValues(32.dp),
        onDevtoolsUnlocked = onDevtoolsUnlocked,
        onPlayFavoriteSong = PlayerState::playFromId,
    )
}
