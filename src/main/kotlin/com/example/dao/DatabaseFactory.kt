package com.example.dao

import com.example.models.Articles
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClass").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString()

        Database.connect(jdbcURL, driverClassName)
        transaction {
            SchemaUtils.create(Articles)
        }
    }

//    private fun crceateHikariDataSource(
//        url: String,
//        driver: String
//    ) = HikariDataSource(HikariConfig())
}

suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T =
        newSuspendedTransaction(Dispatchers.IO, statement = block)
