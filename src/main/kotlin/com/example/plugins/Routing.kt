package com.example.plugins

import com.example.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureRouting() {
    routing {
        staticResources(remotePath = "/static", basePackage = "files")

        get("/") {
            call.respondRedirect("articles")
        }
        route("/articles") {
            get {
                val articles = dao.allArticles()
                call.respond(
                    FreeMarkerContent(
                        template = "index.ftl",
                        model = mapOf("articles" to articles)
                    )
                )
            }
            get("new") {
                call.respond(FreeMarkerContent("new.ftl", model = null))
            }
            post {
                val formParameters = call.receiveParameters()
                val title = formParameters.getOrFail("title")
                val body = formParameters.getOrFail("body")

                val createdArticle = dao.addNewArticle(title, body)
                call.respondRedirect("/articles/${createdArticle!!.id}")
            }
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id")
                val article = dao.article(id)
                    ?: return@get call.respondText("No such article", status = HttpStatusCode.NotFound)

                call.respond(FreeMarkerContent("show.ftl", mapOf("article" to article)))
            }
            get("{id}/edit") {
                val id = call.parameters.getOrFail<Int>("id")
                val article = dao.article(id)
                    ?: return@get call.respondText("Article not found", status = HttpStatusCode.NotFound)
                call.respond(
                    FreeMarkerContent(
                        template = "edit.ftl",
                        model = mapOf("article" to article)
                    )
                )
            }
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id")
                val formParameters = call.receiveParameters()
                val action = formParameters.getOrFail("_action")
                when (action) {
                    "update" -> {
                        val title = formParameters.getOrFail("title")
                        val body = formParameters.getOrFail("body")

                        dao.editArticle(id, title, body)

                        call.respondRedirect("/articles/${id}")
                    }

                    "delete" -> {
                        dao.deleteArticle(id)
                        call.respondRedirect("/articles")
                    }

                    else -> throw RuntimeException("Unknown action ${action}")
                }
            }
        }
    }
}
