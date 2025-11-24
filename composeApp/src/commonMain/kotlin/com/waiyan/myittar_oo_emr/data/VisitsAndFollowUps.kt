package com.waiyan.myittar_oo_emr.data

import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Visit

data class VisitsAndFollowUps(
    val visits: List<Visit>,
    val followUps: List<FollowUp>
)
