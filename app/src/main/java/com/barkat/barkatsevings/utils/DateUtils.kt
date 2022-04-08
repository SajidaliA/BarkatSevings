package com.barkat.barkatsevings.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Sajid Ali Suthar
 */

fun getDateFromString(date: String): Date?{
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return format.parse(date)
}

fun getStringFromDate(date: Date): String{
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val date = Date()
    return format.format(date)
}