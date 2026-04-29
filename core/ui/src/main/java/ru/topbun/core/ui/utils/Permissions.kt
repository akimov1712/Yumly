package ru.topbun.core.ui.utils

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun permissions(vararg permissions: String) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {  }

    val notGrandePermissions = permissions.toList().filter {
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(Unit) {
        if (notGrandePermissions.isNotEmpty()) {
            permissionLauncher.launch(notGrandePermissions.toTypedArray())
        }
    }
}
