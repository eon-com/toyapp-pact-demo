package com.toyapp.pact.demo.customer

import com.toyapp.pact.demo.common.withCustomConfiguration
import com.toyapp.pact.demo.customer.CustomerServiceConfig.segment
import com.toyapp.pact.demo.customer.TestData.getAllContentOrById
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf

object MockHttpClient {

    private val headers = headersOf(HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))

    private val expr = "(/$segment(/(\\d+))?)".toRegex()

    fun build() = HttpClient(MockEngine) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                withCustomConfiguration()
            }
        }

        engine {
            addHandler { request ->
                val result: MatchResult? = expr.find(request.url.toString())
                val id: String? = result?.destructured?.component3()

                when {
                    // .../customers
                    result != null && id.isNullOrEmpty() -> {
                        respond(
                                content = getAllContentOrById(),
                                headers = headers
                        )
                    }
                    // .../customers/<id>
                    result != null && !id.isNullOrEmpty() -> {
                        respond(
                                content = getAllContentOrById(id.toInt()),
                                headers = headers
                        )
                    }
                    // others
                    else -> error("Unhandled ${request.url}")
                }
            }
        }
    }

}