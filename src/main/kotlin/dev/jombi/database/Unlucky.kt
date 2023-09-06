package dev.jombi.database

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

@Serializable
data class Unlucky(
    val id: Int,

    val firstName: String,

    val secondName: String,

    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
)

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

}

object UnluckyTable : Table() {
    val firstName = varchar("firstName", 4)
    val secondName = varchar("secondName", 4)
    val date = date("date")
    val id = integer("id").autoIncrement()

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}