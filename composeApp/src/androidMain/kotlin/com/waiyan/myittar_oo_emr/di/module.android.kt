package com.waiyan.myittar_oo_emr.di

import com.waiyan.myittar_oo_emr.database.getDatabaseBuilder
import com.waiyan.myittar_oo_emr.local.database.EmrDatabase
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<EmrDatabase> { getDatabaseBuilder(androidContext()).build() }
    }