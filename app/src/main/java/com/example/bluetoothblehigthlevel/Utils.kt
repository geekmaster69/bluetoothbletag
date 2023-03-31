package com.example.bluetoothblehigthlevel

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

fun Context.hasPermissions(permissionsType: String): Boolean{
    return ContextCompat.checkSelfPermission(this,
    permissionsType) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasRequiredRuntimePermissions(): Boolean{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN) &&

                hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)
    } else{
        hasPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}

fun BluetoothGatt.printGattTable(){
    if (services.isEmpty()){

        Log.i("printGattTable", "No service and characteristic available, call discoverServices() first?")
        return
    }

    services.forEach { service ->
        val characteriticsTable =
            service.characteristics.joinToString(
                separator = "\n|--",
                prefix = "|--"
            ){it.uuid.toString()}
        Log.i("printGattTable", "\nService ${service.uuid}\nCharacteristics:\n$characteriticsTable")
    }
}


fun BluetoothGattCharacteristic.isReadable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)

fun BluetoothGattCharacteristic.isWritable(): Boolean =
containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE)

fun BluetoothGattCharacteristic.isWritableWithoutResponse(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)

fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean {
    return properties and property != 0
}

fun ByteArray.toHexString(): String =
    joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }

