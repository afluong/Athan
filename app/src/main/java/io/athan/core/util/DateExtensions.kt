package io.athan.core.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Long.formattedStringDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(timeZone).date

    val day = localDate.day.toString().padStart(2, '0')
    val year = localDate.year

    val monthName = localDate.month.name.lowercase()
        .replaceFirstChar { it.uppercase() }

    return "$day $monthName $year"
}

fun Long.toLocalTime(timeZone: TimeZone = TimeZone.currentSystemDefault()) =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(timeZone)
        .time

fun String.toEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
    val parts = this.trim().split(" ")
    require(parts.size == 3) { "Invalid format" }

    val day = parts[0].toInt()
    val year = parts[2].toInt()

    val monthName = parts[1].uppercase()
    val month = Month.valueOf(monthName)

    val localDate = LocalDate(year = year, month = month, day = day)

    return localDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
}