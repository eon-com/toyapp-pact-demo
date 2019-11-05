package com.toyapp.pact.demo.core.data.persistence

import com.toyapp.pact.demo.core.data.CoreDataServiceConfig
import org.sqlite.SQLiteConfig
import org.sqlite.SQLiteDataSource

object DefaultDataSource {

    fun create(): SQLiteDataSource {
        val dataSource = SQLiteDataSource(SQLiteConfig())
        dataSource.url = CoreDataServiceConfig.dataSourceUrl
        return dataSource
    }

}