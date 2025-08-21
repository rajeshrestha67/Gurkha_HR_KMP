package com.gurkha.hr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform