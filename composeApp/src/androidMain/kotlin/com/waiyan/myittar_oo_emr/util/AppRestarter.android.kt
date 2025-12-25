package com.waiyan.myittar_oo_emr.util

import android.content.Intent
import com.waiyan.myittar_oo_emr.di.AndroidAppContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun triggerAppRestart() {
    object : KoinComponent {
        private val appContext: AndroidAppContext by inject()

        fun restart() {
            val context = appContext.context
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent!!.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }.restart()
}
