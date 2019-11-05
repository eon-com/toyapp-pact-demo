package com.toyapp.pact.demo.creditcheck

import assertk.assertThat
import assertk.assertions.isEqualTo
import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.model.RequestResponsePact
import com.toyapp.pact.demo.creditcheck.CreditCheckConfig.segment
import com.toyapp.pact.demo.creditcheck.CreditCheckConfig.version
import org.apache.http.HttpStatus
import org.apache.http.client.fluent.Request
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PactConsumerTestExt::class)
class CreditCheckPactTest {

    @Pact(provider = "core-data-service", consumer = "creditcheck")
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
    }

    private val bodyForAllCustomers = PactDslJsonBody()
            .integerType("page", 1)
            .minArrayLike("payload", 3)
            .integerType("id", 555)
            .stringMatcher("date_of_birth", "\\d{4,4}-\\d{2,2}-\\d{2,2}", "1980-01-01")
            .closeObject()
            .closeArray()
            .close()

    private val bodyForSingleCustomer = PactDslJsonBody()
            .integerType("id", 555)
            .stringType("date_of_birth", "1947-07-30")
            .close()

}