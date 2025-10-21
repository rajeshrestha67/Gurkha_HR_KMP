package com.gurkha.hr.components.permissions

expect class ProgressNotification() {
    suspend fun preloadImage(uriOrPath: String)
    suspend fun showNotification(progress: Int)
}
