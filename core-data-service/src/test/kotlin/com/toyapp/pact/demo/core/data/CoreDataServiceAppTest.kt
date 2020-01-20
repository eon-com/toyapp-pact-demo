package com.toyapp.pact.demo.core.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.toyapp.pact.demo.common.JSONResourceLoader
import com.toyapp.pact.demo.common.PaginationResponse
import com.toyapp.pact.demo.common.withCustomConfiguration
import com.toyapp.pact.demo.core.data.extensions.PersistenceExtension
import com.toyapp.pact.demo.core.data.persistence.Persistence
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PersistenceExtension::class)
class CoreDataServiceAppTest {

    companion object {
        private val mapper = jacksonObjectMapper().withCustomConfiguration()
        private val customers = JSONResourceLoader.loadCollectionFromResource("/fixtures/data/all.json", Customer::class.java)
    }

    @BeforeEach
    fun beforeEach() {
        runBlocking {
            Persistence.writeCustomers(customers)
        }
    }

    @Test
    fun `request without id should return all customers`() = withTestApplication({
        module()
    }) {
        with(handleRequest(HttpMethod.Get, "/v1/customers")) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo(mapper.writeValueAsString(PaginationResponse(page = 1, payload = customers)))
        }
    }

    @Test
    fun `request without id 666 should return selected customer`() = withTestApplication({
        module()
    }) {
        with(handleRequest(HttpMethod.Get, "/v1/customers/666")) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo(mapper.writeValueAsString(customers.firstOrNull { it.id == 666 }))
        }
    }

}