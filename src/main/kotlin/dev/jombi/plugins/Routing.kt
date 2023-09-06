package dev.jombi.plugins

import dev.jombi.dao.IUnluckyDAO
import dev.jombi.dao.UnluckyDAO
import dev.jombi.dto.request.NewUnlucky
import dev.jombi.dto.response.FailedResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }

    val unluckyRepository: IUnluckyDAO = UnluckyDAO()

    routing {
        post("/unlucky") {
            if (unluckyRepository.getUnluckyToday() != null)
                return@post call.respond(HttpStatusCode.Conflict, FailedResponse("Today's unlucky exists"))
            val unlucky = try {
                call.receive<NewUnlucky>()
            } catch (e: Exception) {
                e.printStackTrace()
                return@post call.respond(HttpStatusCode.BadRequest, FailedResponse("Body is null or not enough."))
            }
            val orNull = unluckyRepository.newUnlucky(unlucky.firstName, unlucky.secondName)
                ?: return@post call.respond(HttpStatusCode.InternalServerError, FailedResponse("Some fail"))
            call.respond(HttpStatusCode.OK, orNull)
        }

        get("/unlucky") {
            val unlucky = unluckyRepository.getUnluckyToday()
                ?: return@get call.respond(HttpStatusCode.NotFound, FailedResponse("no today's unlucky"))

            call.respond(HttpStatusCode.OK, unlucky)
        }

        get("/history") {
            val json = unluckyRepository.getUnluckyList()
            if (json.isEmpty())
                return@get call.respond(HttpStatusCode.NotFound, FailedResponse("no histories"))
            call.respond(HttpStatusCode.OK, json)
        }
    }
}
