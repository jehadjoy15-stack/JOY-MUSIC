package com.joymusic.shared.api.lyrics.romanizer

import com.joymusic.shared.utils.XdgUtils
import java.io.File

internal actual fun japaneseDictionaryStorageDirectory(): File = File(XdgUtils.getXdgDataHome(), "joymusic/japanese-dictionary")
