package com.barkat.barkatsevings.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.barkatsevings.R
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Sajid Ali Suthar
 */

fun FragmentActivity.addReplaceFragment(
    @IdRes container: Int,
    fragment: Fragment,
    addFragment: Boolean,
    addToBackStack: Boolean,
    animate: Boolean
) {
    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    if (animate){
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
    }
    if (addFragment) {
        transaction.add(container, fragment, fragment.javaClass.simpleName)
    } else {
        transaction.replace(container, fragment, fragment.javaClass.simpleName)
    }
    if (addToBackStack) {
        transaction.addToBackStack(fragment.tag)
    }

    hideKeyboard()
    transaction.commit()
}

//hide the keyboard
fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) view = View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


//show the keyboard
fun Activity.showKeyboard() {
    val view = currentFocus
    val methodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun checkConnection(context: Context): Boolean {
    val manager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = manager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}

fun showSnackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}