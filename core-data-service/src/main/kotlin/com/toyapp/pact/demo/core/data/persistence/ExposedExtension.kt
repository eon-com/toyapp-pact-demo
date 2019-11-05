package com.toyapp.pact.demo.core.data.persistence

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

open class ExposedExtension {

    suspend fun <T: Table> init(dataSource: DataSource, vararg tables: T) {
        Database.connect(dataSource)
        Persistence.dbQuery {
            SchemaUtils.create(*tables)
        }
    }

    protected suspend fun <T> dbQuery(
            block: () -> T): T =
            withContext(Dispatchers.IO) {
                transaction { block() }
            }

    protected suspend fun truncate(vararg tableNames: String) {
        dbQuery {
            tableNames.forEach {
                TransactionManager.current().exec("DELETE FROM $it")
            }
        }
    }

}