/**
 * Created by Ulises on 3/5/23.
 */
package com.gmail.uli153.workdaymeter.utils.extensions

import java.util.*

fun Date.millisSince(date: Date): Long {
    return this.time - date.time
}