package com.toyapp.pact.demo.core.data.persistence

import com.toyapp.pact.demo.common.Address
import com.toyapp.pact.demo.common.Country
import com.toyapp.pact.demo.core.data.Customer
import com.toyapp.pact.demo.core.data.converters.DateConverters.toJavaLocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.slf4j.LoggerFactory
import javax.sql.DataSource

object Persistence : ExposedExtension() {

    private val logger = LoggerFactory.getLogger(this::class.java.name)!!

    suspend fun init(dataSource: DataSource, customers: List<Customer> = emptyList()) {
        super.init(dataSource, Tables.CustomerTable, Tables.AddressTable)
        if (customers.isNotEmpty()) {
            writeCustomers(customers = customers)
        }
    }

    suspend fun truncate() {
        super.truncate(*Tables.values.map { it.tableName }.toTypedArray())
    }

    suspend fun writeCustomer(customer: Customer) {
        writeCustomers(listOf(customer))
    }

    suspend fun writeCustomers(customers: List<Customer>) {
        dbQuery {
            customers.forEach { customer ->
                if (!customerExists(customer.id)) {
                    Tables.CustomerTable.insert { toRow(customer, it) }
                    customer.address?.let { address -> Tables.AddressTable.insert { toRow(customer.id, address, it) } }
                }
            }
        }
    }

    suspend fun readCustomer(id: Int): Customer? {
        return readCustomers(id).firstOrNull()
    }

    suspend fun readCustomers(id: Int? = null): List<Customer> {
        return getJoinResultRowsGroupedByCustomerIds(id).map { customerResultRows ->
            val firstEntry = customerResultRows.value[0]
            Customer(
                    id = firstEntry[Tables.CustomerTable.id],
                    firstName = firstEntry[Tables.CustomerTable.firstName],
                    lastName = firstEntry[Tables.CustomerTable.lastName],
                    dateOfBirth = firstEntry[Tables.CustomerTable.dateOfBirth].toJavaLocalDate(),
                    address = mapJoinResultRowsToAddresses(customerResultRows.value).getOrNull(0)
            )
        }
    }

    private fun customerExists(customerId: Int): Boolean {
        return Tables.CustomerTable
                .slice(Tables.CustomerTable.id)
                .select { Tables.CustomerTable.id eq customerId }
                .map { it[Tables.CustomerTable.id] }
                .firstOrNull() != null
    }

    private suspend fun getJoinResultRowsGroupedByCustomerIds(id: Int?): Map<Int, List<ResultRow>> {
        return dbQuery {
            val join = Join(
                    Tables.CustomerTable, Tables.AddressTable,
                    onColumn = Tables.CustomerTable.id, otherColumn = Tables.AddressTable.customerId,
                    joinType = JoinType.LEFT
            )
            val query = id?.let { join.select(Tables.CustomerTable.id eq it) }
                    ?: join.selectAll()
            query.map { it }.groupBy { it[Tables.CustomerTable.id] }
        }
    }

    private fun mapJoinResultRowsToAddresses(joinResultRows: List<ResultRow>): List<Address> {
        val addresses = joinResultRows.filter { it.getOrNull(Tables.AddressTable.customerId) != null }.map {
            Address(
                    street = it[Tables.AddressTable.street],
                    number = it[Tables.AddressTable.number],
                    zipCode = it[Tables.AddressTable.zipCode],
                    place = it[Tables.AddressTable.place],
                    country = Country.valueOf(it[Tables.AddressTable.country].toUpperCase())
            )
        }
        if (addresses.size > 1) {
            logger.warn("Customer has more than one address, taking first one")
        }

        return addresses
    }

}


