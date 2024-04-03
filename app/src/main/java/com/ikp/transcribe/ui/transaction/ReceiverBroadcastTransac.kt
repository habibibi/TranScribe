package com.ikp.transcribe.ui.transaction

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReceiverBroadcastTransac() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context,"Broadcast received",Toast.LENGTH_SHORT).show()
        val intent = Intent(context,AddTransactionActivity::class.java)
        intent.putExtra("random","true")
        context.startActivity(intent)
    }
}