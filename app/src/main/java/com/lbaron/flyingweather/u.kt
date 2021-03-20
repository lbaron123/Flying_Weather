package com.lbaron.flyingweather

import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast

/**
 * Utils Object - for each activity must set u.context first
 * Then can use u.l to log
 */
object u {
    fun l(context: Context, message: String?){
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.i(getApplicationName(context), message!!)
    }
    fun e(context: Context, message: String?){
        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
        Log.e(getApplicationName(context), message!!)
    }

    private fun getApplicationName(context: Context): String {
        val applicationInfo: ApplicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

    /**
     * Function to ask if we are connected to the internet
     * Returns a Boolean
     * @param context Context
     */
    fun isNetworkAvailable(context: Context) : Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //NEW PHONES
            // False things
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false //elvis, return right if left is null
            //True thing
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            //OLD PHONES
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}

//private fun metarFromJson(metarResponseJsonString: String?): MetarResponse? {
//    u.l(this, "In metarFromJson function")
//    u.l(this, "Input: $metarResponseJsonString")
//    return null
//}