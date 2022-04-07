package com.apolis.myapplication.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.lang.StringBuilder

//manifest-register BroadcastReceiver

//system can start the app and deliver the broadcast if the app is not currently running.

//The system creates a new BroadcastReceiver component object to handle each broadcast that it receives. This object is valid only for the duration of the call to onReceive(Context, Intent). Once your code returns from this method, the system considers the component no longer active.
private const val Tag = "MyBroadcastReceiver"
class MyBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString().also { log ->
                Log.d(Tag, log)
                Toast.makeText(context, log, Toast.LENGTH_LONG).show()
            }
        }
    }
}