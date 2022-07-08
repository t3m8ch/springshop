package io.github.t3m8ch.springshop.exceptions

data class APIErrorBody(
    val errorCode: String,
    val description: String,
)
