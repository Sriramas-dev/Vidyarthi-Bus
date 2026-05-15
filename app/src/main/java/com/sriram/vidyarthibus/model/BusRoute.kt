package com.sriram.vidyarthibus.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BusRoute(
    var routeId: String = "",
    var routeName: String = "",
    var crowdStatus: String = "EMPTY",
    var lastUpdated: Long = 0L,
    var reporterLat: Double = 0.0,
    var reporterLng: Double = 0.0,
    var college: String = "",
    @get:Exclude @set:Exclude var dbKey: String = ""
) {
    fun isStale(): Boolean {
        return false
    }
}