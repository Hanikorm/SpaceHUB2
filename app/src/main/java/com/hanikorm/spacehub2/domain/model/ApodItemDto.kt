package com.hanikorm.spacehub2.domain.model

import com.google.gson.annotations.SerializedName

// Этот класс — точная копия структуры твоего JSON
data class ApodItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("url") val url: String
)