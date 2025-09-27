package com.waiyan.myittar_oo_emr

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyittarOoEMR",
    ) {
        App()
    }
}