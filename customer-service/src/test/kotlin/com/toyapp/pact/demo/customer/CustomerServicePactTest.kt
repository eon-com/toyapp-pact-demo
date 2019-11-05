package com.toyapp.pact.demo.customer

import assertk.assertThat
import assertk.assertions.isEqualTo
import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.model.RequestResponsePact
import com.toyapp.pact.demo.customer.CustomerServiceConfig.segment
import com.toyapp.pact.demo.customer.CustomerServiceConfig.version
import org.apache.http.HttpStatus
import org.apache.http.client.fluent.Request
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PactConsumerTestExt::class)
class CustomerServicePactTest {

    @Pact(provider = "core-data-service", consumer = "customer-service")
    fun createPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder
                .given("all test customers are available")
                .uponReceiving("get request on /$version/$segment")
                .path("/$version/$segment")
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.SC_OK)
                .body(bodyForAllCustomers)
                .given("Arnold Schwarzenegger is available")
                .uponReceiving("get request on /$version/$segment/555")
                .path("/$version/$segment/555")
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.SC_OK)
                .body(bodyForSingleCustomer)
                .given("there is no customer with id 0")
                .uponReceiving("get request on /$version/$segment/0")
                .path("/$version/$segment/0")
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.SC_NOT_FOUND)
                .given("there is no customer with invalid id a")
                .uponReceiving("get request on /$version/$segment/a")
                .path("/$version/$segment/a")
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.SC_BAD_REQUEST)
                .body(badRequestBody)
                .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    @Throws(Exception::class)
    internal fun test(mockServer: MockServer) {
        val allCustomersResponse = Request.Get(mockServer.getUrl() + "/$version/$segment").execute().returnResponse()
        assertThat(allCustomersResponse.statusLine.statusCode).isEqualTo(200)

        val singleCustomerResponse = Request.Get(mockServer.getUrl() + "/$version/$segment/555").execute().returnResponse()
        assertThat(singleCustomerResponse.statusLine.statusCode).isEqualTo(200)

        val noCustomerResponse = Request.Get(mockServer.getUrl() + "/$version/$segment/0").execute().returnResponse()
        assertThat(noCustomerResponse.statusLine.statusCode).isEqualTo(404)

        val badRequestResponse = Request.Get(mockServer.getUrl() + "/$version/$segment/a").execute().returnResponse()
        assertThat(badRequestResponse.statusLine.statusCode).isEqualTo(400)
    }

    private val bodyForAllCustomers = PactDslJsonBody()
            .integerType("page", 1)
            .minArrayLike("payload", 3)
            .integerType("id", 555)
            .stringMatcher("first_name", ".+", "Any")
            .stringMatcher("last_name", ".+", "Any")
            .closeObject()
            .closeArray()
            .close()

    private val bodyForSingleCustomer = PactDslJsonBody()
            .integerType("id", 555)
            .stringType("first_name", "Arnold")
            .stringType("last_name", "Schwarzenegger")
            .close()

    private val badRequestBody = PactDslJsonBody()
            .stringType("message", "Invalid parameter 'id': must be numerical")
            .close()

}