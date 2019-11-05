package com.toyapp.pact.demo.creditcheck

import com.toyapp.pact.demo.common.Address
import com.toyapp.pact.demo.common.require
import java.time.LocalDate

data class Customer(val id: Int, val dateOfBirth: LocalDate, val address: Address? = null) {
    init {
        mapOf("id" to id).require("must be >= 1") {
            (it as Int) >= 1
        }
    }
}