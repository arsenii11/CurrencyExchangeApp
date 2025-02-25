package com.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            Intent.ACTION_AIRPLANE_MODE_CHANGED ->  {
                Toast.makeText(context, "Airplane mode changed",Toast.LENGTH_SHORT).show()
            }
        }
    }
}