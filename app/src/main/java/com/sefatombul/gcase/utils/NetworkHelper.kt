package com.sefatombul.gcase.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber


object NetworkHelper {

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


    fun internetConnectChangeListener(
        context: Context,
        onAvailable: (network: Network) -> Unit = {},
        onLost: (network: Network) -> Unit = {}
    ) {
        val networkCallback: ConnectivityManager.NetworkCallback = object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                onAvailable(network)
                Timber.e("onAvailable")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                onLost(network)
                Timber.e("onLost")
            }
        }
        val connectivityManager =
            context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.registerDefaultNetworkCallback(networkCallback)
            } else {
                val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                it.registerNetworkCallback(request, networkCallback)
            }
        }
    }

}