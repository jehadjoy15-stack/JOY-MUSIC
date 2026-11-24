package com.joymusic.shared.ui

import com.joymusic.shared.api.UploadFailure
import com.joymusic.shared.api.UploadSongResult
import com.joymusic.shared.generated.resources.Res
import com.joymusic.shared.generated.resources.upload_already_exists
import com.joymusic.shared.generated.resources.upload_empty_file
import com.joymusic.shared.generated.resources.upload_failed
import com.joymusic.shared.generated.resources.upload_http_error
import com.joymusic.shared.generated.resources.upload_sign_in_required
import com.joymusic.shared.generated.resources.upload_too_large
import com.joymusic.shared.generated.resources.upload_unsupported_type
import org.jetbrains.compose.resources.getString

suspend fun UploadSongResult.localizedErrorMessage(): String =
    when (failure) {
        UploadFailure.SIGN_IN_REQUIRED -> getString(Res.string.upload_sign_in_required)
        UploadFailure.UNSUPPORTED_TYPE -> getString(Res.string.upload_unsupported_type)
        UploadFailure.EMPTY_FILE -> getString(Res.string.upload_empty_file)
        UploadFailure.TOO_LARGE -> getString(Res.string.upload_too_large)
        UploadFailure.ALREADY_UPLOADED -> getString(Res.string.upload_already_exists)
        UploadFailure.HTTP -> httpStatus?.let { getString(Res.string.upload_http_error, it) } ?: getString(Res.string.upload_failed)
        else -> getString(Res.string.upload_failed)
    }
