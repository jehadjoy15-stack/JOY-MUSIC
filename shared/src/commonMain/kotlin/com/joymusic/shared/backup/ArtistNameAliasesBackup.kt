package com.joymusic.shared.backup

import com.joymusic.shared.constants.ArtistNameAliasesJsonKey
import com.joymusic.shared.settings.ArtistNameAliases
import com.joymusic.shared.settings.PortableSetting
import com.joymusic.shared.settings.PortableSettings
import com.joymusic.shared.settings.SettingType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun PortableSettings?.withLegacyArtistNameAliases(serialized: String): PortableSettings {
    val aliases = ArtistNameAliases.deserialize(serialized)
    val settings = this ?: PortableSettings(entries = emptyList())
    return settings.copy(
        entries =
            settings.entries.filterNot { it.key == ArtistNameAliasesJsonKey } +
                PortableSetting(ArtistNameAliasesJsonKey, SettingType.STRING, Json.encodeToString(aliases)),
    )
}
