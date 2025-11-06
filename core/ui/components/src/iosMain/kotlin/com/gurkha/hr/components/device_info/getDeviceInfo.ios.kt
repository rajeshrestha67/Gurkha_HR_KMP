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
        uid = "",
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
//
//@OptIn(ExperimentalForeignApi::class)
//private fun getDeviceId(): String {
//    val key = "APP_DEVICE_ID"
//
//    memScoped {
//        // Query dictionary
//        val query = mutableMapOf<CFStringRef?, Any?>(
//            kSecClass to kSecClassGenericPassword,
//            kSecAttrAccount to key,
//            kSecReturnData to kCFBooleanTrue
//        )
//
//        val queryDict = query.toNSDictionary()
//        val resultPtr = alloc<CFTypeRefVar>()
//
//        val status = SecItemCopyMatching(queryDict, resultPtr.ptr)
//
//        // ✅ Found – return stored ID
//        if (status == errSecSuccess) {
//            val data = resultPtr.value as NSData
//            return NSString.create(data, NSUTF8StringEncoding) as String
//        }
//
//        // ✅ Not found – generate new UUID and store
//        val newId = NSUUID().UUIDString()
//
//        val insertQuery = mutableMapOf<CFStringRef?, Any?>(
//            kSecClass to kSecClassGenericPassword,
//            kSecAttrAccount to key,
//            kSecValueData to (newId as NSString).dataUsingEncoding(NSUTF8StringEncoding)!!
//        )
//
//        val insertDict = insertQuery.toNSDictionary()
//        SecItemAdd(insertDict, null)
//
//        return newId
//    }
//}


/*

class KeychainHelper {
    static let accessGroup = UserDefaultsProvider.sharedGroupName


    static func save(key: String, data: Data) -> OSStatus {
        let query = [
            kSecClass as String       : kSecClassGenericPassword as String,
            kSecAttrAccount as String : key,
            kSecValueData as String   : data,
            kSecAttrAccessGroup as String: accessGroup
        ] as [String : Any]

        SecItemDelete(query as CFDictionary)

        return SecItemAdd(query as CFDictionary, nil)
    }

    static func load(key: String) -> Data? {
        let query = [
            kSecClass as String       : kSecClassGenericPassword as String,
            kSecAttrAccount as String : key,
            kSecReturnData as String  : kCFBooleanTrue!,
            kSecMatchLimit as String  : kSecMatchLimitOne,
            kSecAttrAccessGroup as String: accessGroup
        ] as [String : Any]

        var item: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &item)

        if status == noErr, let data = item as? Data {
            return data
        }

        return nil
    }

}

 */