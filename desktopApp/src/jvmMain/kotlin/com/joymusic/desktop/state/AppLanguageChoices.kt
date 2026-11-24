package com.joymusic.desktop.state

import androidx.compose.runtime.Composable
import com.joymusic.shared.generated.resources.Res
import com.joymusic.shared.generated.resources.app_language_system
import com.joymusic.shared.ui.settings.SettingsChoice
import org.jetbrains.compose.resources.stringResource

@Composable
fun appLanguageChoices(): List<SettingsChoice> =
    DesktopLocaleManager.languages().map { language ->
        SettingsChoice(
            language.tag,
            when {
                language.isSystem -> stringResource(Res.string.app_language_system)
                else -> requireNotNull(language.displayName)
            },
        )
    }
