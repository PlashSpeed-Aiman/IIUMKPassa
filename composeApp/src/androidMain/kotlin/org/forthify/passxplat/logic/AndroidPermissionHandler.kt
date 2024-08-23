package org.forthify.passxplat.logic

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import org.forthify.passxplat.MainActivity

class AndroidPermissionHandler(private val activity: Lazy<MainActivity>) : IPermissionHandler {
    private val requestPermissionLauncher = activity.value.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        onResult(isGranted)
    }

    private lateinit var onResult: (Boolean) -> Unit

    override fun requestPermission(permission: String, onResult: (Boolean) -> Unit) {
        this.onResult = onResult
        requestPermissionLauncher.launch(permission)
    }

    override fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity.value,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}