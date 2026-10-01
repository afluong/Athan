package io.athan.feature.location.data.remote.api

import io.athan.feature.location.data.remote.dto.PhotonResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PhotonApiService(
    private val client: HttpClient
) {
    suspend fun getPlacePredictions(
        query: String,
        language: String = DEFAULT_LANGUAGE
    ): PhotonResponseDto =
        client.get(
            urlString = "$URL/api/"
        ) {
            parameter("q", query)
            parameter("lang", language)
            parameter("osm_tag", "place:city")
        }.body()


    companion object {
        private const val URL = "https://photon.komoot.io"
        private const val DEFAULT_LANGUAGE = "fr"
    }
}