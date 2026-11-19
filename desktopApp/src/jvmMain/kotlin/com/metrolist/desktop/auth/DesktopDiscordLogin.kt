package com.metrolist.desktop.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.metrolist.desktop.utils.AppLogger
import com.metrolist.shared.discord.DiscordAuthSession
import com.metrolist.shared.discord.DiscordAuthSessionStore
import com.metrolist.shared.discord.DiscordRpcManager
import com.metrolist.shared.state.GlobalYouTube
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal object DesktopDiscordLogin {
    val flow =
        DesktopDiscordLoginFlow(
            sessions = DiscordAuthSessionStore(),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
            exchangeCode = { session, code ->
                DiscordRpcManager.init(GlobalYouTube.client)
                val result =
                    withContext(Dispatchers.IO) {
                        DiscordRpcManager.auth.exchangeCode(code, session.pkceVerifier, session.state, session.state)
                    }
                DiscordRpcManager.storeAndConnect(result.accessToken, result.refreshToken, result.expiresInSec)
            },
            onError = { DiscordRpcManager.setError(it) },
        )
}

internal class DesktopDiscordLoginFlow(
    private val sessions: DiscordAuthSessionStore,
    private val scope: CoroutineScope,
    private val exchangeCode: suspend (DiscordAuthSession, String) -> Unit,
    private val onError: (String) -> Unit,
) {
    var session by mutableStateOf(sessions.pending())
        private set
    var exchanging by mutableStateOf(false)
        private set
    private var exchangeJob: Job? = null
    private var lastHandledUrl: String? = null

    fun start(session: DiscordAuthSession) {
        cancel()
        lastHandledUrl = null
        sessions.save(session)
        this.session = session
    }

    fun cancel() {
        exchangeJob?.cancel()
        exchangeJob = null
        sessions.clear()
        session = null
        exchanging = false
        lastHandledUrl = null
    }

    fun handleCallback(url: String): Boolean {
        if (url == lastHandledUrl) {
            AppLogger.info("DiscordLogin", "Duplicate callback ignored")
            return true
        }
        AppLogger.info("DiscordLogin", "handleCallback called with: $url")
        val callback = parseDiscordLoginCallback(url) ?: run {
            AppLogger.warning("DiscordLogin", "Failed to parse callback: $url")
            return false
        }
        val pending = sessions.pending()
        if (pending == null) {
            AppLogger.warning("DiscordLogin", "No pending session found in store")
            if (!exchanging && session != null) {
                session = null
                onError("discord_error_token_exchange_failed")
            }
            return true
        }
        if (parseDiscordLoginCallback(url, pending.redirectUri) == null) {
            AppLogger.warning("DiscordLogin", "Callback redirectUri does not match pending ${pending.redirectUri}")
            return true
        }
        if (callback.state != pending.state) {
            AppLogger.warning("DiscordLogin", "OAuth state mismatch: received ${callback.state}, pending ${pending.state}")
            return true
        }
        lastHandledUrl = url
        sessions.clear()
        when {
            callback.error != null -> {
                session = null
                AppLogger.error("DiscordLogin", "Discord returned error: ${callback.error}, description: ${callback.errorDescription}")
                onError("discord_error_denied")
            }

            callback.code.isNullOrBlank() -> {
                session = null
                AppLogger.error("DiscordLogin", "Discord callback code was null or blank")
                onError("discord_error_token_exchange_failed")
            }

            else -> {
                exchanging = true
                exchangeJob =
                    scope.launch {
                        try {
                            AppLogger.info("DiscordLogin", "Beginning token exchange for code")
                            exchangeCode(pending, callback.code)
                            AppLogger.info("DiscordLogin", "Token exchange successful!")
                        } catch (error: CancellationException) {
                            throw error
                        } catch (e: Exception) {
                            AppLogger.error("DiscordLogin", "Token exchange failed with exception: ${e.message}", e)
                            onError("discord_error_token_exchange_failed")
                        } finally {
                            if (session == pending) {
                                session = null
                                exchanging = false
                            }
                        }
                    }
            }
        }
        return true
    }
}
