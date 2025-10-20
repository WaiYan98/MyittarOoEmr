package com.waiyan.myittar_oo_emr.di

import com.waiyan.myittar_oo_emr.local.database.dao.FollowUpDao
import com.waiyan.myittar_oo_emr.local.database.dao.MedicalInfoDao
import com.waiyan.myittar_oo_emr.local.database.EmrDatabase
import com.waiyan.myittar_oo_emr.local.database.dao.PatientDao
import com.waiyan.myittar_oo_emr.local.database.dao.VisitDao
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.local_service.EmrRepositoryImpl
import com.waiyan.myittar_oo_emr.screen.component.patient_screen.PatientViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val shareModule = module {
    singleOf(::EmrRepositoryImpl) { bind<EmrRepository>() }
    viewModelOf(::PatientViewModel)
    single<PatientDao> { get<EmrDatabase>().getPatientDao() }
    single<MedicalInfoDao> { get<EmrDatabase>().getMedicalInfoDao() }
    single<VisitDao> { get<EmrDatabase>().getVisitDao() }
    single<FollowUpDao> { get<EmrDatabase>().getFollowUpDao() }
}