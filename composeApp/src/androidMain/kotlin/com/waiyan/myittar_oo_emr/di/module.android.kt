package com.waiyan.myittar_oo_emr.di

import com.waiyan.myittar_oo_emr.AndroidAppContext
import com.waiyan.myittar_oo_emr.database.getDatabaseBuilder
import com.waiyan.myittar_oo_emr.local.database.EmrDatabase
import com.waiyan.myittar_oo_emr.data.SettingsStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<EmrDatabase> { getDatabaseBuilder(androidContext()).build() }
        single { AndroidAppContext(androidContext()) }
        single { SettingsStorage(androidContext()) }
    }