package com.joymusic.shared.ui.components

import androidx.compose.runtime.Composable
import com.joymusic.shared.generated.resources.Res
import com.joymusic.shared.generated.resources.menu_repeat_all
import com.joymusic.shared.generated.resources.menu_repeat_off
import com.joymusic.shared.generated.resources.menu_repeat_one
import com.joymusic.shared.generated.resources.repeat_mode_description
import com.joymusic.shared.model.RepeatMode
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun repeatContentDescription(repeatMode: RepeatMode): String =
    stringResource(
        Res.string.repeat_mode_description,
        stringResource(
            when (repeatMode) {
                RepeatMode.OFF -> Res.string.menu_repeat_off
                RepeatMode.ONE -> Res.string.menu_repeat_one
                RepeatMode.ALL -> Res.string.menu_repeat_all
            },
        ),
    )
