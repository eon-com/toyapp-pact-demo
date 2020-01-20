package com.toyapp.pact.demo.core.data.extensions

import com.toyapp.pact.demo.core.data.persistence.DefaultEmbeddedDataSource
import com.toyapp.pact.demo.core.data.persistence.Persistence
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class PersistenceExtension : BeforeAllCallback, AfterEachCallback {

    override fun beforeAll(ext: ExtensionContext?) {
        runBlocking {
            Persistence.init(
                    DefaultEmbeddedDataSource.create(
                            databaseName = "test",
                            databaseUser = "test",
                            databasePassword = "test"
                    )
            )
        }
    }

    override fun afterEach(p0: ExtensionContext?) {
        runBlocking {
            Persistence.truncate()
        }
    }

}