package com.gurkha.hr.components.device_info

import com.gurkha.model.device_info.DeviceInfo
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSProcessInfo
import platform.Foundation.NSTimeZone
import platform.Foundation.currentLocale
import platform.Foundation.localTimeZone
import platform.Foundation.localeIdentifier
import platform.UIKit.UIDevice

actual fun getDeviceInfo(): DeviceInfo {
    val device = UIDevice.currentDevice
    val bundle = NSBundle.mainBundle

    val locale = NSLocale.currentLocale.localeIdentifier
    val timezone = NSTimeZone.localTimeZone.name
    val appVersion = bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
    val appBuild = bundle.objectForInfoDictionaryKey("CFBundleVersion") as? String

    val osVersion = "${device.systemName} ${device.systemVersion}"
    val sdkInt = device.systemVersion
    val deviceName = device.name
    val model = device.model
    val isSimulator = NSProcessInfo.processInfo.environment["SIMULATOR_DEVICE_NAME"] != null




    return DeviceInfo(
        platform = "iOS",
        manufacturer = "Apple",
        model = model,
        osVersion = osVersion,
        sdkInt = sdkInt,
        locale = locale,
        timezone = timezone,
        appVersion = appVersion,
        appBuild = appBuild,
        deviceName = deviceName,
        isEmulator = isSimulator
    )
}
