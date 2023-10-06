package com.colibrez.contacts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform