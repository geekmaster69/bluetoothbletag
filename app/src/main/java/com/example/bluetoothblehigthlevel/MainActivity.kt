package com.example.bluetoothblehigthlevel

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bluetoothblehigthlevel.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var leScanCallback: ScanCallback
    private lateinit var gattCallback: BluetoothGattCallback
    private lateinit var gatt2: BluetoothGatt
    private val handler = Handler()
    private val SCAN_PERIOD: Long = 10000
    private lateinit var characteristicNotify: BluetoothGattCharacteristic
    val NOTIFI_SERVICE: UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    val NOTIFI_CHARACTERISTIC: UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private var isScanning = false

        set(value) {
            field = value
            runOnUiThread { binding.btnScan.text = if (value) "Stop Scan" else "Start Scan" }
        }

    private var bluetoothService: BluetoothLeService? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            bluetoothService = (service as BluetoothLeService.LocalBinder).getService()
            bluetoothService?.let { bluetooth ->
                if (!bluetooth.initialize()){
                    finish()
                }

                // call functions on service to check connection and connect to devices


            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bluetoothService = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gattServiceIntent = Intent(this, BluetoothLeService::class.java)
        bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

          leScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                Log.d("Result", result.toString())

            }
        }



        binding.btnScan.setOnClickListener { startBleScan()}

    }

    private fun startBleScan() {
        if (!hasRequiredRuntimePermissions()){
        }

        if (!isScanning){
            handler.postDelayed({
                isScanning = false
                bleScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            isScanning = true
            bleScanner.startScan(leScanCallback)
        }else{
            isScanning = false
            bleScanner.stopScan(leScanCallback)
        }




/*        if (!isScanning){



            val filter = ScanFilter.Builder()
                .setServiceUuid(
                    ParcelUuid.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
                ).build()

            val scanSetting = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setMatchMode(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                .build()



            scanCallback = object : ScanCallback(){
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    with(result.device){
                        connectGatt(this@MainActivity, false, gattCallback)
                        Log.d("Scan Result", "Connecting to $name")
                        stopScan()


                    }
                }

            }

             gattCallback = object : BluetoothGattCallback(){
                 override fun onConnectionStateChange(
                     gatt: BluetoothGatt,
                     status: Int,
                     newState: Int
                 ) {
                     super.onConnectionStateChange(gatt, status, newState)
                     if (newState == BluetoothProfile.STATE_CONNECTED){


                         gatt.discoverServices()
                     }
                 }

                 override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                     super.onServicesDiscovered(gatt, status)
                     characteristicNotify = gatt.getService(NOTIFI_SERVICE)
                         .getCharacteristic(NOTIFI_CHARACTERISTIC)

                     gatt.setCharacteristicNotification(characteristicNotify, true)
                 }

                 override fun onCharacteristicChanged(
                     gatt: BluetoothGatt,
                     characteristic: BluetoothGattCharacteristic,
                     value: ByteArray
                 ) {
                     super.onCharacteristicChanged(gatt, characteristic, value)
                     if (characteristic.equals(characteristicNotify)){
                         runOnUiThread{
                             Toast.makeText(this@MainActivity, "Click", Toast.LENGTH_SHORT).show()
                         }
                     }
                 }

            }

            val  devFilters: MutableList<ScanFilter> = ArrayList()
            devFilters.add(filter)

            bleScanner.startScan(devFilters, scanSetting, scanCallback)
            isScanning = true

        }else{
            bleScanner.stopScan(scanCallback)
            isScanning = false

        }*/


    }

}