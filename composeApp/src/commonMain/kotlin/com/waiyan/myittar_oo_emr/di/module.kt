package com.waiyan.myittar_oo_emr.di

import com.waiyan.myittar_oo_emr.local.database.EmrDatabase
import com.waiyan.myittar_oo_emr.local.database.PatientDao
import com.waiyan.myittar_oo_emr.local_service.PatientRepository
import com.waiyan.myittar_oo_emr.local_service.PatientRepositoryImpl
import com.waiyan.myittar_oo_emr.screen.PatientViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val shareModule = module {
    singleOf(::PatientRepositoryImpl) { bind<PatientRepository>() }
    viewModelOf(::PatientViewModel)
    single<PatientDao> { get<EmrDatabase>().getDao() }
}