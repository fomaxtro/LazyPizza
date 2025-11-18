package com.fomaxtro.core.presentation.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun countdownTimer(duration: Duration): Flow<Duration> = flow {
    val endTime = System.currentTimeMillis() + duration.inWholeMilliseconds
    var ticks = duration.inWholeSeconds

    emit(duration)

    while (ticks > 0) {
        val currentTime = System.currentTimeMillis()
        val remainingMs = endTime - currentTime

        val millisIntoSecond = remainingMs % 1000L
        delay(millisIntoSecond.coerceAtLeast(10))

        ticks = remainingMs / 1000L
        emit(ticks.seconds)
    }
}.flowOn(Dispatchers.Default)