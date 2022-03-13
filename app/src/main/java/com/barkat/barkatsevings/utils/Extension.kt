package com.barkat.barkatsevings.utils

import android.view.View

/**
 * Created by Sajid Ali Suthar
 */

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun String?.isEmailValid(): Boolean {
    return !this.isNullOrEmpty() && doesStringMatchPattern(this, REGEX_EMAIL)
}

fun String?.isPasswordValid(): Boolean =
    !this.isNullOrEmpty() && this.length >= 6
