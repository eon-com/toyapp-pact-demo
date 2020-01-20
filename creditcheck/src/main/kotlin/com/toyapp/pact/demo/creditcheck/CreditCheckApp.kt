package com.toyapp.pact.demo.creditcheck

import com.toyapp.pact.demo.common.PaginationResponse
import com.toyapp.pact.demo.common.StatusMessage
import com.toyapp.pact.demo.common.withCustomConfiguration
import com.toyapp.pact.demo.creditcheck.CreditCheckConfig.factory
import com.toyapp.pact.demo.creditcheck.CreditCheckConfig.localPort
import com.toyapp.pact.demo.creditcheck.CreditCheckConfig.remotePath
import com.toyapp.pact.demo.creditcheck.CreditCheckConfig.segment
import com.toyapp.pact.demo.creditcheck.CreditCheckConfig.version
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer

class CreditCheckApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = embeddedServer(factory, localPort) {
                moduleWithDeps(DefaultHttpClient.build())
            }
            server.start(wait = true)
        }
    }
}

fun Application.moduleWithDeps(httpClient: HttpClient) {
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            withCustomConfiguration()
        }
    }

    routing {
        get("/$version/$segment") {
            val customers = httpClient.get<PaginationResponse<Customer>>(remotePath)
            call.respond(customers)
        }

        get("/$version/$segment/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            id?.let {
                try {
                    val customer = httpClient.get<Customer>("$remotePath/$it")
                    call.respond(customer)
                } catch (ex: Exception) {
                    // TODO: make http status code dependent of concrete error type
                    call.application.environment.log.error("Cannot retrieve data", ex)
                    call.respond(HttpStatusCode.NotFound)
                }
            } ?: call.respond(HttpStatusCode.BadRequest, StatusMessage("Invalid parameter 'id': must be numerical"))
        }
    }
}