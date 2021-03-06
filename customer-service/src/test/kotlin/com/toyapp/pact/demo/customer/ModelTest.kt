package com.toyapp.pact.demo.customer

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.toyapp.pact.demo.common.Address
import com.toyapp.pact.demo.common.Country
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerEvaluationTest {

    private val testCustomer = Customer(
            id = 178789797,
            firstName = "Franz",
            lastName = "Huber",
            address = Address(
                    street = "Schwestergasse",
                    number = "25",
                    zipCode = "84034",
                    place = "Landshut",
                    country = Country.DE
            )
    )

    @Test
    fun `customer data class creation is working for valid arguments`() {
        assertThat(testCustomer.copy()).isEqualTo(testCustomer)
    }

    @Test
    fun `customer data class creation throws IllegalArgumentException if first name is empty`() {
        val thrown = assertThrows<IllegalArgumentException> {
            testCustomer.copy(firstName = "")
        }
        assertThat(thrown.message).isEqualTo("Argument firstName must not be empty")
    }

    @Test
    fun `customer data class creation throws IllegalArgumentException if id is invalid`() {
        val thrown = assertThrows<IllegalArgumentException> {
            testCustomer.copy(id = 0)
        }
        assertThat(thrown.message).isEqualTo("Argument id must be >= 1")
    }

}