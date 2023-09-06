package dev.jombi.dao

import dev.jombi.database.DatabaseFactory.query
import dev.jombi.database.Unlucky
import dev.jombi.database.UnluckyTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.sql.Date
import java.time.LocalDate
import java.time.ZoneId

class UnluckyDAO : IUnluckyDAO {
    private fun now() = LocalDate.now(ZoneId.of("Asia/Seoul"))
    private fun resultRowToArticle(row: ResultRow) = Unlucky(
        id = row[UnluckyTable.id],
        firstName = row[UnluckyTable.firstName],
        secondName = row[UnluckyTable.secondName],
        date = row[UnluckyTable.date],
    )

    override suspend fun getUnluckyToday(): Unlucky? = query {
        UnluckyTable
            .select { UnluckyTable.date eq now() }
            .map(::resultRowToArticle)
            .singleOrNull()
    }

    override suspend  fun getUnluckyList(): List<Unlucky> = query {
        UnluckyTable.selectAll()
            .map(::resultRowToArticle)
    }

    override suspend fun newUnlucky(firstName: String, secondName: String): Unlucky? = query {
        val inserted = UnluckyTable.insert {
            it[UnluckyTable.firstName] = firstName
            it[UnluckyTable.secondName] = secondName
            it[date] = now()
        }
        inserted.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }
}