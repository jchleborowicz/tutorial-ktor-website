package com.example.models

import java.util.concurrent.atomic.AtomicInteger

class Article
private constructor(val id: Int, var title: String, var body: String) {
    companion object {
        private val idCounter = AtomicInteger()

         fun newEntry(title: String, body: String) =
            Article(
                id = idCounter.getAndIncrement(),
                title = title,
                body = body
            )
    }
}

val articles = mutableListOf(
    Article.newEntry(
        "The drive to develop",
        "...it's what keeps me going."
    )
)
