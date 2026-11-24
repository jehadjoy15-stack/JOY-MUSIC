/*
 * JoyMusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.joymusic.desktop.utils

import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeBytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WindowsAppIdentityTest {
    @Test
    fun discordHandlerUsesPerUserSchemeAndCurrentPortableExecutable() {
        val directory = Files.createTempDirectory("JoyMusic portable żółć ")
        try {
            val launcher = directory.resolve("JoyMusic.exe").also { it.writeBytes(byteArrayOf(1)) }.toFile()
            val values = mutableMapOf<Pair<String, String>, String>()
            assertTrue(
                registerWindowsUriHandler(launcher, "JoyMusic", "joymusic-auth") { key, name, value -> values[key to name] = value },
            )
            val key = "Software\\Classes\\joymusic-auth"
            assertEquals("", values[key to "URL Protocol"])
            assertEquals("\"${launcher.absolutePath}\" \"%1\"", values["$key\\shell\\open\\command" to ""])
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    @Test
    fun uriHandlerQuotesPackagedLauncherAndForwardsOneUrlArgument() {
        val directory = Files.createTempDirectory("JoyMusic żółć ")
        try {
            val launcher = directory.resolve("JoyMusic.exe").also { it.writeBytes(byteArrayOf(1)) }.toFile()
            val values = mutableMapOf<Pair<String, String>, String>()
            registerWindowsUriHandler(launcher, "JoyMusic") { key, name, value -> values[key to name] = value }

            val key = "Software\\Classes\\joymusic"
            assertEquals(
                mapOf(
                    (key to "") to "JoyMusic",
                    (key to "URL Protocol") to "",
                    ("$key\\DefaultIcon" to "") to "\"${launcher.absolutePath}\",0",
                    ("$key\\shell\\open\\command" to "") to "\"${launcher.absolutePath}\" \"%1\"",
                ),
                values,
            )
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    @Test
    fun uriHandlerDoesNotRegisterUnpackagedOrMissingLaunchers() {
        val directory = Files.createTempDirectory("joymusic-uri")
        try {
            val java = directory.resolve("java").also { it.writeBytes(byteArrayOf(1)) }.toFile()
            val candidates = listOf(null, File("JoyMusic.exe"), directory.toFile(), directory.resolve("missing.exe").toFile(), java)
            for (launcher in candidates) {
                registerWindowsUriHandler(launcher, "JoyMusic") { _, _, _ -> error("Unexpected registration for $launcher") }
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }
}
