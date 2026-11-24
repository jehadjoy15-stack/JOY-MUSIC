package com.joymusic.shared.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.joymusic.shared.generated.resources.Res
import com.joymusic.shared.generated.resources.file_size_unit_byte
import com.joymusic.shared.generated.resources.file_size_unit_gigabyte
import com.joymusic.shared.generated.resources.file_size_unit_kilobyte
import com.joymusic.shared.generated.resources.file_size_unit_megabyte
import com.joymusic.shared.generated.resources.file_size_unit_terabyte
import com.joymusic.shared.utils.FileSizeUnits
import com.joymusic.shared.utils.formatFileSize
import org.jetbrains.compose.resources.stringResource

@Composable
fun rememberFileSizeFormatter(): (Long) -> String {
    val units =
        FileSizeUnits(
            byte = stringResource(Res.string.file_size_unit_byte),
            kilobyte = stringResource(Res.string.file_size_unit_kilobyte),
            megabyte = stringResource(Res.string.file_size_unit_megabyte),
            gigabyte = stringResource(Res.string.file_size_unit_gigabyte),
            terabyte = stringResource(Res.string.file_size_unit_terabyte),
        )
    return remember(units) { { bytes -> formatFileSize(bytes, units) } }
}
