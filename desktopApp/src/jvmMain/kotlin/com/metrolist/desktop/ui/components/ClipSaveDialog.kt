package com.metrolist.desktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.metrolist.desktop.export.ClipExporter
import com.metrolist.shared.model.SongItem
import com.metrolist.shared.ui.components.AlertDialog
import com.metrolist.shared.ui.icons.MetrolistIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipSaveDialog(
    song: SongItem,
    onDismiss: () -> Unit,
) {
    val totalSeconds = (song.duration?.toFloat() ?: 180f).coerceAtLeast(10f)
    var sliderRange by remember(song.id) {
        val initialEnd = (30f).coerceAtMost(totalSeconds)
        mutableStateOf(0f..initialEnd)
    }
    var isSaving by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun formatTime(sec: Float): String {
        val s = sec.toInt()
        val m = s / 60
        val rem = s % 60
        return "%d:%02d".format(m, rem)
    }

    val artistName = song.artists.joinToString(", ") { it.name }.ifBlank { "Unknown Artist" }
    val clipDuration = (sliderRange.endInclusive - sliderRange.start).toInt()

    AlertDialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        title = {
            Text(
                "Save Audio / Video Clip",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Song Info Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AsyncImage(
                        model = song.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(52.dp).clip(RoundedCornerShape(8.dp)),
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = artistName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                // Range Slider Section
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "Clip: ${formatTime(sliderRange.start)} - ${formatTime(sliderRange.endInclusive)} (${clipDuration}s)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            "Total: ${formatTime(totalSeconds)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    RangeSlider(
                        value = sliderRange,
                        onValueChange = { range ->
                            val start = range.start.coerceIn(0f, totalSeconds - 1f)
                            val end = range.endInclusive.coerceIn(start + 1f, totalSeconds)
                            sliderRange = start..end
                        },
                        valueRange = 0f..totalSeconds,
                        enabled = !isSaving,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    )

                    // Quick duration chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf(15, 30, 60).forEach { dur ->
                            AssistChip(
                                onClick = {
                                    val start = sliderRange.start
                                    val end = (start + dur).coerceAtMost(totalSeconds)
                                    sliderRange = start..end
                                },
                                label = { Text("+${dur}s", fontSize = 12.sp) },
                                enabled = !isSaving,
                            )
                        }
                        AssistChip(
                            onClick = { sliderRange = 0f..totalSeconds },
                            label = { Text("Full Song", fontSize = 12.sp) },
                            enabled = !isSaving,
                        )
                    }
                }

                // Status or Progress
                if (isSaving) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(12.dp))
                        Text(statusText ?: "Processing...", style = MaterialTheme.typography.bodyMedium)
                    }
                } else if (statusText != null) {
                    Text(
                        text = statusText!!,
                        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Save Audio (.mp3)
                Button(
                    onClick = {
                        val cleanTitle = song.title.replace(Regex("[\\\\/:*?\"<>|]"), "_")
                        val cleanArtist = artistName.replace(Regex("[\\\\/:*?\"<>|]"), "_")
                        val defaultName = "$cleanArtist - $cleanTitle (Clip).mp3"

                        val file = ClipExporter.pickSaveFile(defaultName, "MP3 Audio (*.mp3)", "mp3")
                        if (file != null) {
                            scope.launch {
                                isSaving = true
                                isError = false
                                statusText = "Saving audio to chosen location..."
                                val ok = ClipExporter.saveAudioClip(song, sliderRange.start, sliderRange.endInclusive, file)
                                isSaving = false
                                if (ok) {
                                    statusText = "Audio saved successfully: ${file.name}"
                                } else {
                                    isError = true
                                    statusText = "Failed to save audio clip."
                                }
                            }
                        }
                    },
                    enabled = !isSaving,
                ) {
                    Icon(MetrolistIcons.AutoMirrored.Outlined.VolumeUp, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Save Audio (.mp3)")
                }

                // Save Video (.mp4)
                FilledTonalButton(
                    onClick = {
                        val cleanTitle = song.title.replace(Regex("[\\\\/:*?\"<>|]"), "_")
                        val cleanArtist = artistName.replace(Regex("[\\\\/:*?\"<>|]"), "_")
                        val defaultName = "$cleanArtist - $cleanTitle (Clip).mp4"

                        val file = ClipExporter.pickSaveFile(defaultName, "MP4 Video (*.mp4)", "mp4")
                        if (file != null) {
                            scope.launch {
                                isSaving = true
                                isError = false
                                statusText = "Generating video to chosen location..."
                                val ok = ClipExporter.saveVideoClip(song, sliderRange.start, sliderRange.endInclusive, file)
                                isSaving = false
                                if (ok) {
                                    statusText = "Video saved successfully: ${file.name}"
                                } else {
                                    isError = true
                                    statusText = "Failed to generate video clip."
                                }
                            }
                        }
                    },
                    enabled = !isSaving,
                ) {
                    Icon(MetrolistIcons.Outlined.PlayArrow, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Save Video (.mp4)")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSaving,
            ) {
                Text("Close")
            }
        },
    )
}
