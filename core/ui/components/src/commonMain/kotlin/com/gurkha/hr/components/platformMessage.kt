package com.gurkha.hr.components
//
//interface ShowMessage {
//    fun showToast(message: String)
//}
//
//expect fun getPlatformMessage(): ShowMessage

expect class PlatformMessage {
    fun showToast(message: String)
}
