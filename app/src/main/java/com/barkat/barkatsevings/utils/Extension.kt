package com.barkat.barkatsevings.utils

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.barkatsevings.R

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

fun AppCompatImageView.setImage(url: String, isRound: Boolean = false) {
    if (isRound) {
        Glide.with(this.context)
            .load(url)
            .circleCrop()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(this)
    } else {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.drawable.ic_user_placeholder)
            .into(this)
    }
}
