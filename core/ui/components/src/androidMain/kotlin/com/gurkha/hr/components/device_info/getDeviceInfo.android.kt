package com.gurkha.hr.components.device_info

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.gurkha.model.device_info.DeviceInfo
import org.koin.mp.KoinPlatform.getKoin
import java.util.Locale
import java.util.TimeZone

actual fun getDeviceInfo(): DeviceInfo {
    val context: Context = getKoin().get()
    val pm = context.packageManager
    val packageInfo = pm.getPackageInfo(context.packageName, 0)

    val isEmulator = Build.FINGERPRINT.lowercase().contains("generic")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")

    val appBuild = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode.toString()
    } else {
        @Suppress("DEPRECATION")
        packageInfo.versionCode.toString()
    }
    return DeviceInfo(
        platform = "Android",
        deviceUniqueIdentifier = generateId(context = context),
        manufacturer = Build.MANUFACTURER,
        model = Build.MODEL,
        osVersion = Build.VERSION.RELEASE,
        sdkInt = Build.VERSION.SDK_INT.toString(),
        locale = Locale.getDefault().toString(),
        timezone = TimeZone.getDefault().id,
        appVersion = packageInfo.versionName,
        appBuild = appBuild,
        deviceName = Build.DEVICE,
        isEmulator = isEmulator
    )
}

private fun generateId(context: Context): String {
    return Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

actual fun getDeviceUniqueIdentifier(): String {
    val context: Context = getKoin().get()
    return generateId(context = context)
}