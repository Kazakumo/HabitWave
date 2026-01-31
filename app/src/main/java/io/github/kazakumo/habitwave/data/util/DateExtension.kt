package io.github.kazakumo.habitwave.data.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toYyyyMmDd(): Long {
    return this.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toLong()
}

/**
 * Long 型 (20240520) を LocalDate に戻す（ヒートマップ作成時に使います）
 */
fun Long.toLocalDate(): LocalDate {
    val year = (this / 10000).toInt()
    val month = ((this % 10000) / 100).toInt()
    val day = (this % 100).toInt()
    return LocalDate.of(year, month, day)
}