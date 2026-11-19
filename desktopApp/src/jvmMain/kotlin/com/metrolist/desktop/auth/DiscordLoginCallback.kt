package com.metrolist.desktop.auth

import com.metrolist.shared.discord.DiscordDefaults
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

internal data class DiscordLoginCallback(
    val code: String?,
    val state: String?,
    val error: String?,
    val errorDescription: String? = null,
)

internal fun parseDiscordLoginCallback(
    url: String,
    redirectUri: String = DiscordDefaults.DISCORD_REDIRECT_URI,
): DiscordLoginCallback? =
    runCatching {
        val uri = URI(url)
        val redirect = URI(redirectUri)
        val schemeMatches = uri.scheme.equals(redirect.scheme, ignoreCase = true) ||
            uri.scheme in setOf("joymusic-auth", "metrolist-auth", "joymusicdiscord")
        val hostMatches = uri.rawAuthority.equals(redirect.rawAuthority, ignoreCase = true) ||
            uri.rawAuthority in setOf("discord", "oauth2")
        val pathMatches = uri.rawPath.equals(redirect.rawPath, ignoreCase = true) ||
            uri.rawPath.startsWith("/callback")
        if (!schemeMatches || !hostMatches || !pathMatches || uri.rawFragment != null) {
            return@runCatching null
        }
        val parameters = mutableMapOf<String, String>()
        uri.rawQuery.orEmpty().split('&').filter { '=' in it }.forEach {
            val parts = it.split('=', limit = 2)
            val key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8)
            val value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
            parameters[key] = value
        }
        DiscordLoginCallback(
            parameters["code"],
            parameters["state"],
            parameters["error"],
            parameters["error_description"],
        )
    }.getOrNull()
