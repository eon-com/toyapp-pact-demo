package com.toyapp.pact.demo.core.data

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.loader.PactBroker
import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import com.toyapp.pact.demo.common.JSONResourceLoader
import com.toyapp.pact.demo.core.data.CoreDataServiceConfig.pactBrokerHost
import com.toyapp.pact.demo.core.data.CoreDataServiceConfig.pactBrokerPort
import com.toyapp.pact.demo.core.data.CoreDataServiceConfig.pactBrokerScheme
import com.toyapp.pact.demo.core.data.CoreDataServiceConfig.port
import com.toyapp.pact.demo.core.data.extensions.ApplicationContextExtension
import com.toyapp.pact.demo.core.data.extensions.PersistenceExtension
import com.toyapp.pact.demo.core.data.persistence.Persistence
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import java.net.URL

@ExtendWith(ApplicationContextExtension::class, PersistenceExtension::class)
@Provider("core-data-service")
@PactBroker(scheme = pactBrokerScheme, host = pactBrokerHost, port = pactBrokerPort.toString())
class CoreDataServicePactVerificationTest {

    companion object {
        private const val PROVIDER_URL = "http://localhost:${port}"
    }

    @BeforeEach
    fun beforeEach(context: PactVerificationContext) {
        context.target = HttpTestTarget.fromUrl(URL(PROVIDER_URL))
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun pactVerificationTest(context: PactVerificationContext) {
        context.verifyInteraction();
    }

    @State("all test customers are available")
    fun allCustomersAvailable() {
        val customers = JSONResourceLoader.loadCollectionFromResource("/fixtures/data/all.json", Customer::class.java)
        runBlocking {
            Persistence.writeCustomers(customers)
        }
    }

    @State("Arnold Schwarzenegger is available")
    fun selectedCustomerAvailable() {
        val customer = JSONResourceLoader.loadFromResource("/fixtures/data/555.json", Customer::class.java)
        runBlocking {
            Persistence.writeCustomer(customer)
        }
    }

    @State("there is no customer with id 0")
    fun noCustomerWithId0() {
        // Nothing to do here
    }

    @State("there is no customer with invalid id a")
    fun noCustomerForInvalidId() {
        // Nothing to do here
    }

}

