package com.barkat.barkatsevings.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * Created by Sajid Ali Suthar
 */

class ConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (Listener != null) {
            val isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting
            Listener!!.onNetworkChange(isConnected)
        }
    }

    interface ReceiverListener {
        fun onNetworkChange(isConnected: Boolean)
    }

    companion object {
        var Listener: ReceiverListener? = null
    }
}
