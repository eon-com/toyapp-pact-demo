package com.toyapp.pact.demo.core.data.persistence

import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource

object DefaultEmbeddedDataSource {

    fun create(databaseName: String, databaseUser: String, databasePassword: String): DataSource {
        val dataSource = JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:$databaseName;DB_CLOSE_DELAY=-1")
        dataSource.user = databaseUser
        dataSource.password = databasePassword
        return dataSource
    }

}