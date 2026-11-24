package com.joymusic.desktop.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.joymusic.desktop.state.app.AppState
import com.joymusic.desktop.state.app.DesktopNavigation
import com.joymusic.desktop.ui.screens.settings.ListenTogetherSettingsContent
import com.joymusic.shared.listentogether.ListenTogetherManager
import com.joymusic.shared.settings.Settings
import com.joymusic.shared.ui.navigation.Screen
import kotlinx.coroutines.launch
import com.joymusic.shared.ui.listentogether.ListenTogetherScreen as SharedListenTogetherScreen

@Composable
fun ListenTogetherScreen(manager: ListenTogetherManager) {
    var showSettings by remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        onDispose { DesktopNavigation.listenTogetherRoomCode = null }
    }

    if (!Settings.shared.listenTogetherEnabled && !showSettings) {
        LaunchedEffect(Unit) { DesktopNavigation.navigate(Screen.Home) }
        return
    }

    if (showSettings) {
        ListenTogetherSettingsContent(
            manager = manager,
            onNavigateBack = { showSettings = false },
        )
    } else {
        SharedListenTogetherScreen(
            manager = manager,
            initialRoomCode = DesktopNavigation.listenTogetherRoomCode,
            onNavigateBack = null,
            onNavigateToSettings = { showSettings = true },
            onCopyText = { _, text -> AppState.copyToClipboard(text) },
            onEventMessage = { message, _ -> AppState.scope.launch { AppState.snackbarHostState.showSnackbar(message) } },
        )
    }
}
