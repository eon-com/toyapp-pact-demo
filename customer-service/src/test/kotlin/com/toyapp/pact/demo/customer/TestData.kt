package com.toyapp.pact.demo.customer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.toyapp.pact.demo.common.PaginationResponse
import com.toyapp.pact.demo.common.withDefaultConfiguration

object TestData {

    private val mapper = jacksonObjectMapper().withDefaultConfiguration()

    private val customers = PaginationResponse(payload = listOf(
            Customer(555, "Arnold", "Schwarzenegger"),
            Customer(666, "Bruce", "Willis"),
            Customer(777, "Sylvester", "Stalone")
    ))

    fun getAllContentOrById(id: Int? = null): String {
        return mapper.writeValueAsString(id?.let { myId -> customers.payload.firstOrNull { it.id == myId } }
                ?: customers)
    }

}