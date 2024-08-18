package org.forthify.passxplat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform