/**
 * Created by Ulises on 27/4/23.
 */
package com.gmail.uli153.workdaymeter.utils.extensions

val Long.formattedTime: String get() {
//    if (this < 0) throw IllegalArgumentException("Number must be >= 0")
    if (this < 0) return "00:00:00"
    val s = String.format("%02d", this % 60)
    val m = String.format("%02d", (this / 60) % 60)
    val h = String.format("%02d", this / 3600)
    return "$h:$m:$s"
}