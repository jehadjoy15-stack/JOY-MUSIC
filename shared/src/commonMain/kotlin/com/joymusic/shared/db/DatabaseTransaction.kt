package com.joymusic.shared.db

import androidx.room3.withWriteTransaction

suspend fun <R> MusicDatabase.withDatabaseTransaction(block: suspend () -> R): R = withWriteTransaction { block() }
