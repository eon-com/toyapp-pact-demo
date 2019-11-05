package com.toyapp.pact.demo.customer

import com.toyapp.pact.demo.common.Address
import com.toyapp.pact.demo.common.require

data class Customer(val id: Int, val firstName: String, val lastName: String, val address: Address? = null) {
    init {
        mapOf("id" to id).require("must be >= 1") {
            (it as Int) >= 1
        }
        mapOf("firstName" to firstName, "lastName" to lastName).require("must not be empty") {
            (it as String).isNotEmpty()
        }
    }
}