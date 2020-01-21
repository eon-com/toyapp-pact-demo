package com.toyapp.pact.demo.creditcheck

import io.ktor.server.netty.Netty

object CreditCheckConfig {

    // TODO: use config framework/files of Ktor instead
    const val localPort = 8083
    val factory = Netty

    private const val remoteProtocol = "http"
    private const val remoteBaseUrl = "127.0.0.1"
    private const val remotePort = 8082
    private const val remoteSegment = "customers"

    const val remotePath = "$remoteProtocol://$remoteBaseUrl:$remotePort/$remoteSegment"
    const val segment = "customers"

}
