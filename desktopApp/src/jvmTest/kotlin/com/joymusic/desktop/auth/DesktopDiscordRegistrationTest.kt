package com.joymusic.desktop.auth

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DesktopDiscordRegistrationTest {
    @Test
    fun desktopEntryQuotesLauncherAndKeepsUrlAsOneArgument() {
        val launcher = "/opt/JoyMusic żółć/AppImage"
        val entry = linuxDiscordDesktopEntry(launcher, "JoyMusic")
        assertTrue("Exec=env \"$launcher\" %u\n" in entry)
        assertTrue("MimeType=x-scheme-handler/joymusic-auth;\n" in entry)
        assertEquals(
            listOf("xdg-mime", "default", "com.joymusic.desktop.discord-auth.desktop", "x-scheme-handler/joymusic-auth"),
            linuxDiscordRegistrationCommand(),
        )
    }

    @Test
    fun desktopEntryEscapesExecMetacharactersAndRejectsNewlines() {
        val entry = linuxDiscordDesktopEntry("/tmp/a\"b\$c`d\\e%f", "JoyMusic")
        val escaped = "/tmp/a" + "\\\\" + "\"b" + "\\\\" + "\$c" + "\\\\" + "`d" + "\\\\\\\\" + "e%%f"
        assertTrue("Exec=env \"$escaped\" %u\n" in entry)
        assertFailsWith<IllegalArgumentException> { linuxDiscordDesktopEntry("/tmp/a\nExec=bad", "JoyMusic") }
        assertFailsWith<IllegalArgumentException> { linuxDiscordDesktopEntry("/tmp/app", "JoyMusic\nExec=bad") }
    }

    @Test
    fun appImageUsesPersistentPathAndNativeLaunchUsesEnvironmentWrapper() {
        val directory = Files.createTempDirectory("joymusic-registration ").toFile()
        try {
            fun executable(name: String) =
                File(directory, name).apply {
                    writeText("")
                    setExecutable(true)
                }
            val appImage = executable("JoyMusic.AppImage")
            val native = executable("JoyMusic.bin")
            val wrapper = executable("JoyMusic")
            val java = executable("java")
            assertEquals(appImage, linuxDiscordLauncher(appImage.path, native.path))
            assertEquals(wrapper, linuxDiscordLauncher(null, native.path))
            assertNull(linuxDiscordLauncher(null, java.path))
            assertNull(linuxDiscordLauncher(null, "JoyMusic"))
            assertNull(linuxDiscordLauncher(File(directory, "missing.AppImage").path, native.path))
        } finally {
            directory.deleteRecursively()
        }
    }
}
