package com.toyapp.pact.demo.customer

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.toyapp.pact.demo.customer.CustomerServiceConfig.segment
import com.toyapp.pact.demo.customer.CustomerServiceConfig.version
import com.toyapp.pact.demo.customer.TestData.getAllContentOrById
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test

class CustomerServiceAppTest {

    @Test
    fun `proxy request without id should return all customers`() = withTestApplication({
        moduleWithDeps(
                httpClient = MockHttpClient.build()
        )
    }) {
        with(handleRequest(HttpMethod.Get, "/$version/$segment")) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo(getAllContentOrById())
        }
    }

    @Test
    fun `proxy request without id 666 should return selected customer`() = withTestApplication({
        moduleWithDeps(
                httpClient = MockHttpClient.build()
        )
    }) {
        with(handleRequest(HttpMethod.Get, "/$version/$segment/666")) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo(getAllContentOrById(666))
        }
    }

}