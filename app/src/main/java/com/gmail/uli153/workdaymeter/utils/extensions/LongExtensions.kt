/**
 * Created by Ulises on 27/4/23.
 */
package com.gmail.uli153.workdaymeter.utils.extensions

val Long.formattedTime: String get() {
//    if (this < 0) throw IllegalArgumentException("Number must be >= 0")
    if (this < 0) return "00:00:00"
    val seconds = this / 1000
    val s = String.format("%02d", seconds % 60)
    val m = String.format("%02d", (seconds / 60) % 60)
    val h = String.format("%02d", seconds / 3600)
    return "$h:$m:$s"
}

val Long.formattedTimeChart: String get() {
//    if (this < 0) throw IllegalArgumentException("Number must be >= 0")
    if (this <= 0) return "0s"
    val seconds = this / 1000
    val ss = seconds % 60
    val min = (seconds / 60) % 60
    val hours = seconds / 3600
    val s = if (ss > 0 && hours < 1) String.format("%ds", ss) else ""
    val m = if (min > 0) String.format("%dm", min) else ""
    val h = if (hours > 0) String.format("%dh", hours) else ""
//    val hm = if (h.isNotEmpty() && m.isNotEmpty()) "$h:$m" else "$h$m"
//    val hms = if (hm.isNotEmpty() && s.isNotEmpty()) "$hm:$s" else "$hm$s"
    val hms = "$h$m$s"
    return hms.ifEmpty { "0s" }
}