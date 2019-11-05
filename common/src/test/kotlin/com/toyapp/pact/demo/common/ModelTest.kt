package com.toyapp.pact.demo.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerEvaluationTest {

    private val address = Address(
            street = "Schwestergasse",
            number = "25",
            zipCode = "84034",
            place = "Landshut",
            country = Country.DE
    )

    @Test
    fun `address data class creation is working for valid arguemnts`() {
        assertThat(address.copy()).isEqualTo(address)
    }

    @Test
    fun `customer data class creation throws IllegalArgumentException if first name is empty`() {
        val thrown = assertThrows<IllegalArgumentException> {
            address.copy(street = "")
        }
        assertThat(thrown.message).isEqualTo("Argument street must not be empty")
    }

}