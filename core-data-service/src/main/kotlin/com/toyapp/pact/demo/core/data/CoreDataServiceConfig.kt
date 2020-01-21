package com.toyapp.pact.demo.core.data

import io.ktor.server.netty.Netty

object CoreDataServiceConfig {

    // TODO: use config framework/files of Ktor instead
    const val port = 8082
    val factory = Netty

    const val segment = "customers"

    const val pactBrokerScheme = "http"
    const val pactBrokerHost = "localhost"
    const val pactBrokerPort = 8080

    const val databaseName = "test"
    const val databaseUser = "sa"
    const val databasePasword = "sa"

}
