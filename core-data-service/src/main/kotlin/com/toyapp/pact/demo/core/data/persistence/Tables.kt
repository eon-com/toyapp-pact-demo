package com.toyapp.pact.demo.core.data.persistence

import com.toyapp.pact.demo.common.Address
import com.toyapp.pact.demo.core.data.Customer
import com.toyapp.pact.demo.core.data.converters.DateConverters.toJodaDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object Tables {

    val values = listOf(AddressTable, CustomerTable)

    object CustomerTable : Table("customers") {
        val id = integer("id").primaryKey().uniqueIndex()
        val firstName = varchar("first_name", 50)
        val lastName = varchar("last_name", 50)
        val dateOfBirth = date("date_of_birth")

        fun toRow(customer: Customer, st: UpdateBuilder<Int>) {
            st[id] = customer.id
            st[firstName] = customer.firstName
            st[lastName] = customer.lastName
            st[dateOfBirth] = customer.dateOfBirth.toJodaDateTime()
        }
    }

    object AddressTable : Table("addresses") {
        val customerId = integer("customer_id").references(CustomerTable.id)
        val street = varchar("street", 50)
        val number = varchar("number", 20)
        val zipCode = varchar("zipcode", 10)
        val place = varchar("place", 50)
        val country = varchar("country", 2)

        fun toRow(pCustomerId: Int, address: Address, st: UpdateBuilder<Int>) {
            st[customerId] = pCustomerId
            st[street] = address.street
            st[number] = address.number
            st[zipCode] = address.zipCode
            st[place] = address.place
            st[country] = address.country.name.toUpperCase()
        }
    }

}