package io.athan.feature.prayersschedule.data.remote

import io.athan.feature.prayersschedule.data.remote.dto.AladhanResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AladhanApiService(
    private val client: HttpClient
) {
    suspend fun getPrayerTimes(
        timeStampSeconds: Long,
        lat: Double,
        lng: Double
    ): AladhanResponseDto =
        client.get(
            urlString =
                "$URL/$VERSION/timings/$timeStampSeconds?latitude=$lat&longitude=$lng",
        )
            .body()

    companion object {
        private const val URL = "https://api.aladhan.com"
        private const val VERSION = "v1"
    }
}