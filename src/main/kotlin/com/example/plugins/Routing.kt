package com.example.plugins

import com.example.models.articles
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources(remotePath = "/static", basePackage = "files")

        get("/") {
            call.respondRedirect("articles")
        }
        route("/articles") {
            get {
                call.respond(
                    FreeMarkerContent(
                        template = "index.ftl",
                        model = mapOf("articles" to articles)
                    )
                )
            }
            get("new") {

            }
            post {

            }
            get("{id}") {

            }
            get("{id}/edit") {

            }
            post("{id}") {

            }
        }

    }
}
