package com.gildor.firstvpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor

class FirstVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        establishVpn()

        return START_STICKY
    }

    private fun establishVpn() {

        vpnInterface?.close()

        vpnInterface = Builder()
            .setSession("FIRST VPN")
            .addAddress("10.8.0.2", 32)
            .addRoute("0.0.0.0", 0)
            .setMtu(1400)
            .establish()
    }

    override fun onDestroy() {

        vpnInterface?.close()
        vpnInterface = null

        super.onDestroy()
    }
}
