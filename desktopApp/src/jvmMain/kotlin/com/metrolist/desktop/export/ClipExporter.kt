package com.metrolist.desktop.export

import com.metrolist.desktop.storage.DownloadManager
import com.metrolist.desktop.storage.SongCacheManager
import com.metrolist.shared.model.SongItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object ClipExporter {
    fun findFfmpeg(): String {
        val userHome = System.getProperty("user.home")
        val wingetDir = File(userHome, "AppData/Local/Microsoft/WinGet/Packages")
        if (wingetDir.exists()) {
            val match = wingetDir.walkTopDown().firstOrNull { it.name.equals("ffmpeg.exe", ignoreCase = true) }
            if (match != null && match.exists()) return match.absolutePath
        }
        return "ffmpeg"
    }

    suspend fun resolveAudioSource(song: SongItem): String? {
        val local = DownloadManager.getLocalAudioPath(song.id)
        if (local != null && File(local).isFile && File(local).length() > 1024) return local

        val cached = SongCacheManager.getCachedFilePath(song.id)
        if (cached != null && File(cached).isFile && File(cached).length() > 1024) return cached

        return withContext(Dispatchers.IO) {
            runCatching {
                DownloadManager.resolvePlaybackResource(song, refresh = false)?.url
            }.getOrNull()
        }
    }

    fun pickSaveFile(defaultName: String, description: String, extension: String): File? {
        val chooser = JFileChooser().apply {
            dialogTitle = "Select Destination to Save"
            selectedFile = File(defaultName)
            fileFilter = FileNameExtensionFilter(description, extension)
        }
        val result = chooser.showSaveDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            var file = chooser.selectedFile
            if (!file.name.endsWith(".$extension", ignoreCase = true)) {
                file = File(file.parentFile, "${file.name}.$extension")
            }
            return file
        }
        return null
    }

    suspend fun saveAudioClip(song: SongItem, startSec: Float, endSec: Float, outputFile: File): Boolean {
        val input = resolveAudioSource(song) ?: return false
        val ffmpeg = findFfmpeg()
        val duration = (endSec - startSec).coerceAtLeast(0.5f)

        return withContext(Dispatchers.IO) {
            runCatching {
                val cmd = mutableListOf(
                    ffmpeg,
                    "-y",
                    "-ss", startSec.toString(),
                    "-i", input,
                    "-t", duration.toString(),
                    "-vn",
                    "-c:a", "libmp3lame",
                    "-b:a", "256k",
                    outputFile.absolutePath
                )
                val process = ProcessBuilder(cmd).redirectErrorStream(true).start()
                val exitCode = process.waitFor()
                exitCode == 0 && outputFile.exists() && outputFile.length() > 0
            }.getOrDefault(false)
        }
    }

    suspend fun saveVideoClip(song: SongItem, startSec: Float, endSec: Float, outputFile: File): Boolean {
        val input = resolveAudioSource(song) ?: return false
        val ffmpeg = findFfmpeg()
        val duration = (endSec - startSec).coerceAtLeast(0.5f)

        return withContext(Dispatchers.IO) {
            runCatching {
                val thumbTemp = File.createTempFile("thumb_", ".jpg")
                try {
                    val thumbUrl = song.thumbnail
                    if (!thumbUrl.isNullOrBlank()) {
                        java.net.URI(thumbUrl).toURL().openStream().use { inputS ->
                            thumbTemp.outputStream().use { outputS ->
                                inputS.copyTo(outputS)
                            }
                        }
                    } else {
                        val blank = BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB)
                        ImageIO.write(blank, "jpg", thumbTemp)
                    }

                    val cmd = mutableListOf(
                        ffmpeg,
                        "-y",
                        "-loop", "1",
                        "-i", thumbTemp.absolutePath,
                        "-ss", startSec.toString(),
                        "-i", input,
                        "-t", duration.toString(),
                        "-c:v", "libx264",
                        "-tune", "stillimage",
                        "-c:a", "aac",
                        "-b:a", "192k",
                        "-pix_fmt", "yuv420p",
                        "-vf", "scale=720:720",
                        "-shortest",
                        outputFile.absolutePath
                    )
                    val process = ProcessBuilder(cmd).redirectErrorStream(true).start()
                    val exitCode = process.waitFor()
                    exitCode == 0 && outputFile.exists() && outputFile.length() > 0
                } finally {
                    thumbTemp.delete()
                }
            }.getOrDefault(false)
        }
    }
}
