package com.mshetty.stockpilot.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.mshetty.stockpilot.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NoInternetException(context.getString(R.string.error_no_internet))
        }
        return chain.proceed(chain.request())
    }
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo?.isConnectedOrConnecting == true
        }
    }
}

class NoInternetException(message: String) : IOException(message)
