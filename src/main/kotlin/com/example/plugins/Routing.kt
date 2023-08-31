package com.example.plugins

import com.example.models.Article
import com.example.models.articles
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
                val newEntry = Article.newEntry(title, body)
                articles.add(newEntry)
                call.respondRedirect("/articles/${newEntry.id}")
            }
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id")
                val article = articles.find { it.id == id } ?: return@get call.respondText(
                    "No such article",
                    status = HttpStatusCode.NotFound
                )
                call.respond(FreeMarkerContent("show.ftl", mapOf("article" to article)))
            }
            get("{id}/edit") {
                val id = call.parameters.getOrFail<Int>("id")
                val article = articles.find { it.id == id }
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
                        val index = articles.indexOf(articles.find { it.id == id })
                        val title = formParameters.getOrFail("title")
                        val body = formParameters.getOrFail("body")
                        articles[index].title = title
                        articles[index].body = body
                        call.respondRedirect("/articles/${id}")
                    }

                    "delete" -> {
                        articles.removeIf { it.id == id }
                        call.respondRedirect("/articles")
                    }

                    else -> throw RuntimeException("Unknown action ${action}")
                }
            }
        }
    }
}
