package com.example.dao

import com.example.models.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {
    private var connected = false

    fun init() {
        if (connected) throw RuntimeException("Already connected")
        connected = true

        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        Database.connect(jdbcURL, driverClassName)
        transaction {
            SchemaUtils.create(Articles)
        }
    }
}

suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
