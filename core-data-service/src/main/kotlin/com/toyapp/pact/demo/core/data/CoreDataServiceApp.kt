package com.toyapp.pact.demo.core.data

import com.toyapp.pact.demo.common.JSONResourceLoader
import com.toyapp.pact.demo.common.PaginationResponse
import com.toyapp.pact.demo.common.StatusMessage
import com.toyapp.pact.demo.common.withCustomConfiguration
import com.toyapp.pact.demo.core.data.CoreDataServiceConfig.factory
import com.toyapp.pact.demo.core.data.CoreDataServiceConfig.port
import com.toyapp.pact.demo.core.data.CoreDataServiceConfig.segment
import com.toyapp.pact.demo.core.data.persistence.DefaultEmbeddedDataSource
import com.toyapp.pact.demo.core.data.persistence.Persistence
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
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
import kotlinx.coroutines.runBlocking

class CoreDataServiceApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val customers = JSONResourceLoader.loadCollectionFromResource("/migrations/data/initial.json", Customer::class.java)
            runBlocking {
                Persistence.init(
                        DefaultEmbeddedDataSource.create(
                                databaseName = CoreDataServiceConfig.databaseName,
                                databaseUser = CoreDataServiceConfig.databaseUser,
                                databasePassword = CoreDataServiceConfig.databasePasword
                        ),
                        customers
                )
            }
            val server = embeddedServer(factory, port) {
                module()
            }
            server.start(wait = true)
        }
    }
}

fun Application.module() {
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            withCustomConfiguration()
        }
    }

    routing {
        get("/$segment") {
            call.respond(PaginationResponse(1, Persistence.readCustomers()))
        }

        get("/$segment/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            id?.let {
                val customer = Persistence.readCustomer(id = it)
                if (customer == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(customer)
                }
            } ?: call.respond(HttpStatusCode.BadRequest, StatusMessage("Invalid parameter 'id': must be numerical"))
        }
    }
}