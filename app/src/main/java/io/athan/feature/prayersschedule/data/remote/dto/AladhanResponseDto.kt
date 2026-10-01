package io.athan.feature.prayersschedule.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AladhanResponseDto(
    @SerialName("code") val code: Int,
    @SerialName("status") val status: String,
    @SerialName("data") val data: AladhanDataDto,
)

@Serializable
data class AladhanDataDto(
    @SerialName("timings") val timings: Map<String, String>)
