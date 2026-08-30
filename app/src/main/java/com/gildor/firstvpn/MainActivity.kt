package com.gildor.firstvpn

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var statusText: TextView
    private lateinit var connectButton: Button
    private lateinit var serverSpinner: Spinner

    private var connected = false

    private val servers = listOf(
        "🇺🇸 United States — New York",
        "🇺🇸 United States — Los Angeles",
        "🇺🇸 United States — Chicago",
        "🇺🇸 United States — Dallas",
        "🇺🇸 United States — Miami",
        "🇳🇱 Netherlands — Amsterdam",
        "🇩🇪 Germany — Frankfurt"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        connectButton = findViewById(R.id.connectButton)
        serverSpinner = findViewById(R.id.serverSpinner)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            servers
        )

        serverSpinner.adapter = adapter

        connectButton.setOnClickListener {

            if (connected) {
                disconnectVpn()
            } else {
                requestVpnPermission()
            }
        }

        updateUi()
    }

    private fun requestVpnPermission() {

        val intent = VpnService.prepare(this)

        if (intent != null) {

            startActivityForResult(
                intent,
                VPN_PERMISSION_REQUEST
            )

        } else {

            startVpn()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == VPN_PERMISSION_REQUEST &&
            resultCode == RESULT_OK
        ) {

            startVpn()
        }
    }

    private fun startVpn() {

        val selectedServer =
            serverSpinner.selectedItem.toString()

        val intent =
            Intent(this, FirstVpnService::class.java)

        intent.putExtra(
            "server",
            selectedServer
        )

        startService(intent)

        connected = true

        updateUi()
    }

    private fun disconnectVpn() {

        stopService(
            Intent(this, FirstVpnService::class.java)
        )

        connected = false

        updateUi()
    }

    private fun updateUi() {

        if (connected) {

            statusText.text =
                getString(R.string.connected)

            connectButton.text =
                getString(R.string.disconnect)

        } else {

            statusText.text =
                getString(R.string.not_connected)

            connectButton.text =
                getString(R.string.connect)
        }
    }

    companion object {

        private const val VPN_PERMISSION_REQUEST = 100
    }
}
