package com.lbaron.flyingweather.models

import java.io.Serializable
import java.sql.Timestamp

class Meta (
        val timestamp: String,
        val stations_updated: String
    ) : Serializable
