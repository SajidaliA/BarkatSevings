package com.barkat.barkatsevings.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

/**
 * Created by Sajid Ali Suthar
 */

@BindingAdapter("setImage")
fun setImage(imageView: AppCompatImageView, url : String?){
    url?.let {
        imageView.setImage(url, true)
    }
}