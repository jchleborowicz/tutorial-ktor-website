package com.example.dao

import com.example.models.Articles
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClass").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString()

        val dataSource: DataSource = createHikariDataSource(jdbcURL, driverClassName)

        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(Articles)
        }
    }

    private fun createHikariDataSource(
        jdbcUrl: String,
        driverClassName: String
    ): HikariDataSource = HikariConfig()
        .apply {
            this.driverClassName = driverClassName
            this.jdbcUrl = jdbcUrl
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }
        .let(::HikariDataSource)
}

suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
