package com.hanikorm.spacehub2.domain.model

import com.google.gson.annotations.SerializedName

data class ApodEntry(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("url") val imageResName: String
)