package com.gurkha.hr.networkhelper


sealed class BaseUrl(open val url: String) {
    data object Generic : BaseUrl("mbank.gurkhahr.com")
}