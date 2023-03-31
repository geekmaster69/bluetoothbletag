package com.example.bluetoothblehigthlevel

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log


class BluetoothLeService : Service() {

    private var bluetoothAdapter: BluetoothAdapter? = null

    fun initialize(): Boolean{
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null){
            Log.e("ServiceBLuetooth", "Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }

/*
    fun connect(address: String): Boolean{
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
            }catch (e: IllegalStateException){
                Log.w()
            }

        }
    }
*/





    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {

        return binder

    }

    inner class LocalBinder: Binder(){
        fun getService(): BluetoothLeService{
            return this@BluetoothLeService
        }
    }
}