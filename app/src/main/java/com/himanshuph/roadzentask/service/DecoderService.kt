package com.himanshuph.roadzentask.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.himanshuph.roadzentask.service.DecoderService.LocalBinder
import java.util.*


class DecoderService : Service() {

    // Binder given to clients
     val binder = LocalBinder()
    var timer: Timer? = null
    var count = -1
    // Registered callbacks
    var serviceCallback: ServiceCallback? = null


    fun startTimerTask() {
        val handler = Handler();
        timer = Timer()
        val backtask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        Log.d("CHECK", Thread.currentThread().toString());
                        count++;
                        serviceCallback?.decodeAddress()
                        if (count == 5) {
                            timer?.cancel();
                            timer = null;
                            serviceCallback?.enableBtn()
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
        timer?.schedule(backtask, 0, 10000); //execute in every 20000 ms*/
    }


    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun setCallbacks(callbacks: ServiceCallback?) {
        serviceCallback = callbacks
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // Class used for the client Binder.
    inner class LocalBinder : Binder() {
        val decoderService: DecoderService
            get() = this@DecoderService
    }
}
