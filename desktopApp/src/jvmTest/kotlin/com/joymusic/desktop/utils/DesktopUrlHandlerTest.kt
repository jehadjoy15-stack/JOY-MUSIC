/**
 * JoyMusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.joymusic.desktop.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DesktopUrlHandlerTest {
    @Test
    fun recognizesDiscordCallbacksForStartupAndSecondLaunchDelivery() {
        assertTrue(DesktopUrlHandler.canHandle("\"joymusic-auth://discord/callback?code=code&state=state\""))
        assertFalse(DesktopUrlHandler.canHandle("joymusic-auth://discord.evil/callback?code=code&state=state"))
        assertFalse(DesktopUrlHandler.canHandle("joymusic-auth://discord/callback-extra?code=code&state=state"))
    }

    @Test
    fun normalizesSurroundingQuotesAndWhitespace() {
        assertEquals(
            "https://music.youtube.com/watch?v=R93xV3v718s",
            DesktopUrlHandler.normalizeUrl("  \"https://music.youtube.com/watch?v=R93xV3v718s\"  "),
        )
        assertEquals(
            "https://music.youtube.com/watch?v=R93xV3v718s",
            DesktopUrlHandler.normalizeUrl("'https://music.youtube.com/watch?v=R93xV3v718s'"),
        )
    }

    @Test
    fun normalizesJoyMusicCustomSchemeUrls() {
        assertEquals(
            "https://music.youtube.com/watch?v=R93xV3v718s",
            DesktopUrlHandler.normalizeUrl("joymusic://music.youtube.com/watch?v=R93xV3v718s"),
        )
        assertEquals(
            "https://music.youtube.com/watch?v=R93xV3v718s",
            DesktopUrlHandler.normalizeUrl("joymusic://https://music.youtube.com/watch?v=R93xV3v718s"),
        )
        assertEquals(
            "https://music.youtube.com/watch?v=R93xV3v718s",
            DesktopUrlHandler.normalizeUrl("joymusic://open?url=https%3A%2F%2Fmusic.youtube.com%2Fwatch%3Fv%3DR93xV3v718s"),
        )
    }

    @Test
    fun returnsNullForBlankInput() {
        assertNull(DesktopUrlHandler.normalizeUrl(""))
        assertNull(DesktopUrlHandler.normalizeUrl("   "))
        assertNull(DesktopUrlHandler.normalizeUrl("\"\""))
    }

    @Test
    fun canHandleIdentifiesValidMusicUrls() {
        assertTrue(DesktopUrlHandler.canHandle("https://music.youtube.com/browse/MPSP_show?si=shared"))
        assertFalse(DesktopUrlHandler.canHandle("https://youtube.com.example/browse/MPSP_show"))
        assertTrue(DesktopUrlHandler.canHandle("https://music.youtube.com/watch?v=R93xV3v718s"))
        assertTrue(DesktopUrlHandler.canHandle("https://music.youtube.com/playlist?list=PL1234567890abcdef"))
        assertTrue(DesktopUrlHandler.canHandle("https://music.youtube.com/browse/MPREb_1234567890"))
        assertTrue(DesktopUrlHandler.canHandle("https://music.youtube.com/channel/UC1234567890abcdef"))
        assertTrue(DesktopUrlHandler.canHandle("https://joymusic.cc/listen?code=ROOM1234"))
        assertTrue(DesktopUrlHandler.canHandle("joymusic://listen?code=ROOM1234"))
        assertTrue(DesktopUrlHandler.canHandle("joymusic://listen-together/ROOM1234"))
        assertTrue(DesktopUrlHandler.canHandle("https://joymusic.cc/watch?v=R93xV3v718s"))
        assertFalse(DesktopUrlHandler.canHandle("http://localhost:8080/watch?v=R93xV3v718s"))
        assertTrue(DesktopUrlHandler.canHandle("joymusic://watch?v=R93xV3v718s"))
        assertTrue(DesktopUrlHandler.canHandle("joymusic://watch?v=123456789"))
        assertTrue(DesktopUrlHandler.canHandle("joymusic://party?r=listentogetherid"))
        assertTrue(DesktopUrlHandler.canHandle("https://joymusic.cc/party?r=listentogetherid"))
        assertFalse(DesktopUrlHandler.canHandle("https://feat-redesign2-metrosite.joymusic.workers.dev/party?r=listentogetherid"))
        assertFalse(DesktopUrlHandler.canHandle("https://feat-redesign2-metrosite.joymusic.workers.dev/watch?v=R93xV3v718s"))
        assertFalse(DesktopUrlHandler.canHandle("http://localhost:8080/party?r=listentogetherid"))
    }

    @Test
    fun canHandleRejectsInvalidUrls() {
        assertFalse(DesktopUrlHandler.canHandle(null))
        assertFalse(DesktopUrlHandler.canHandle(""))
        assertFalse(DesktopUrlHandler.canHandle("https://google.com"))
        assertFalse(DesktopUrlHandler.canHandle("https://127.0.0.1/watch?v=R93xV3v718s"))
        assertFalse(DesktopUrlHandler.canHandle("https://127.0.0.1/listen?code=ROOM1234"))
        assertFalse(DesktopUrlHandler.canHandle("https://youtube.com.attacker.com/watch?v=123"))
        assertFalse(DesktopUrlHandler.canHandle("https://joymusic.cc/listen?code=ROOM12"))
        assertFalse(DesktopUrlHandler.canHandle("joymusic://listen-together/ROOM12345"))
        assertFalse(DesktopUrlHandler.canHandle("joymusic://listen-together/ROOM123!"))
        assertFalse(DesktopUrlHandler.canHandle("random text"))
    }
}
