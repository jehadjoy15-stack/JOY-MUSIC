package com.joymusic.shared.playback

import com.joymusic.shared.model.SongItem

const val QUEUE_SOURCE_ADDED_MANUALLY = "joymusic:queue-source:added-manually"
const val QUEUE_SOURCE_AUTO_MIX = "joymusic:queue-source:auto-mix"
const val QUEUE_SOURCE_RECOMMENDED = "joymusic:queue-source:recommended"

fun String?.isRecommendedQueueSource(): Boolean = this == QUEUE_SOURCE_RECOMMENDED || this == "Recommended"

fun String?.isAutoMixQueueSource(): Boolean = this == QUEUE_SOURCE_AUTO_MIX

fun String?.isRecommendationGeneratedQueueSource(): Boolean = isRecommendedQueueSource() || isAutoMixQueueSource()

fun SongItem.asManuallyAddedQueueItem(): SongItem =
    copy(
        queueSourceTitle =
            queueSourceTitle
                ?.takeUnless(String::isRecommendationGeneratedQueueSource)
                ?: QUEUE_SOURCE_ADDED_MANUALLY,
    )
