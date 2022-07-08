package io.github.t3m8ch.springshop.exceptions

import org.springframework.http.HttpStatus

abstract class BaseAPIException(
    val httpStatus: HttpStatus,
    val body: APIErrorBody,
) : RuntimeException(body.description)
