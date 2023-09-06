package dev.jombi.dao

import dev.jombi.database.Unlucky

interface IUnluckyDAO {
    suspend fun getUnluckyToday(): Unlucky?
    suspend fun getUnluckyList(): List<Unlucky>
    suspend fun newUnlucky(firstName: String, secondName: String): Unlucky?
}