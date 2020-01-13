package com.toyapp.pact.demo.common


fun Map<String, Any>.require(msg: String, block: (Any) -> Boolean) {
    this.entries.forEach {
        require(block(it.value)) {
            "Argument ${it.key} $msg"
        }
    }
}

enum class Country { CH, DE, AT, US, UK }

data class Address(val street: String, val number: String, val zipCode: String, val place: String, val country: Country) {
    init {
        mapOf("street" to street, "number" to number, "zipCode" to zipCode, "place" to place).require("must not be empty") {
            (it as String).isNotEmpty()
        }
    }
}

data class PaginationResponse<T>(val page: Int = 1, val payload: List<T>)

data class StatusMessage(val message: String)