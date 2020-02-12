package com.toyapp.pact.demo.core.data.extensions

import com.toyapp.pact.demo.core.data.CoreDataServiceConfig
import com.toyapp.pact.demo.core.data.module
import io.ktor.server.engine.embeddedServer
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class ApplicationContextExtension : BeforeAllCallback {

    override fun beforeAll(ext: ExtensionContext?) {
        val server = embeddedServer(CoreDataServiceConfig.factory, CoreDataServiceConfig.port) {
            module()
        }
        server.start()
        Runtime.getRuntime().addShutdownHook(Thread {
            server.stop(0, 0)
        })
    }

}