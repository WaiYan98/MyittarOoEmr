package com.waiyan.myittar_oo_emr

import android.app.Application
import com.waiyan.myittar_oo_emr.database.getDatabaseBuilder
import com.waiyan.myittar_oo_emr.di.platformModule
import com.waiyan.myittar_oo_emr.di.shareModule
import com.waiyan.myittar_oo_emr.local.database.EmrDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyittarOoEmrApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(platformModule, shareModule)
        }
    }
}