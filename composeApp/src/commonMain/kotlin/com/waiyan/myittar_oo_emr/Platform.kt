package com.waiyan.myittar_oo_emr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform