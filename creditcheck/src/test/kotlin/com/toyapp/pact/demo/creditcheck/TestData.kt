package com.toyapp.pact.demo.creditcheck

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.toyapp.pact.demo.common.PaginationResponse
import com.toyapp.pact.demo.common.withCustomConfiguration
import java.time.LocalDate

object TestData {

    private val mapper = jacksonObjectMapper().withCustomConfiguration()

    private val customers = PaginationResponse(payload = listOf(
            Customer(555, LocalDate.of(1947, 7, 30)),
            Customer(666, LocalDate.of(1955, 3, 19)),
            Customer(777, LocalDate.of(1946, 7, 6))
    ))

    fun getAllContentOrById(id: Int? = null): String {
        return mapper.writeValueAsString(id?.let { myId -> customers.payload.firstOrNull { it.id == myId } }
                ?: customers)
    }

}