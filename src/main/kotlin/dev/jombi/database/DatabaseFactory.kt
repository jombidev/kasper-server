package dev.jombi.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    init {
        val driver = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./sql.db"
        val db = Database.connect(jdbcURL, driver)
        transaction(db) {
            SchemaUtils.create(UnluckyTable)
        }
    }

    suspend fun <T> query(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}