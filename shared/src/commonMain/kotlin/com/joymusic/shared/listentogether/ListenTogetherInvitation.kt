package com.joymusic.shared.listentogether

import io.ktor.http.URLProtocol
import io.ktor.http.Url

private val roomCodePattern = Regex("^[A-Za-z0-9_-]{1,64}$")

fun parseListenTogetherInvitation(value: String): String? {
    val uri = runCatching { Url(value) }.getOrNull() ?: return null
    val code =
        when {
            (uri.protocol == URLProtocol.HTTPS || uri.protocol == URLProtocol.HTTP) &&
                (uri.host.equals("joymusic.site", ignoreCase = true) || uri.host.endsWith(".joymusic.site", ignoreCase = true) || uri.host.equals("joymusic.cc", ignoreCase = true) || uri.host.endsWith(".joymusic.cc", ignoreCase = true)) &&
                (uri.encodedPath.trim('/') == "listen" || uri.encodedPath.trim('/') == "party" || uri.encodedPath.trim('/') == "room") ->
                uri.parameters["code"] ?: uri.parameters["room"] ?: uri.parameters["r"]

            uri.protocol.name == "joymusic" && (uri.host == "listen-together" || uri.host == "listen" || uri.host == "party" || uri.host == "room") ->
                uri.parameters["code"] ?: uri.parameters["room"] ?: uri.parameters["r"] ?: uri.encodedPath.trim('/')

            else -> null
        }
    return code?.takeIf(roomCodePattern::matches)?.uppercase()
}
