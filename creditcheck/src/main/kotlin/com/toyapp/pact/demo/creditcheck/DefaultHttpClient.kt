package com.toyapp.pact.demo.creditcheck

import com.toyapp.pact.demo.common.withDefaultConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature

object DefaultHttpClient {

    fun build() = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                withDefaultConfiguration()
            }
        }

        engine {
            followRedirects = true
            socketTimeout = 10_000
            connectTimeout = 10_000
            connectionRequestTimeout = 20_000
        }
    }

}