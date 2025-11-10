package com.gurkha.hr.networkhelper


sealed class BaseUrl(open val url: String) {

    data object Generic : BaseUrl("mbank.gurkhahr.com")
//    data object Generic : BaseUrl("192.168.1.145:9092")
}