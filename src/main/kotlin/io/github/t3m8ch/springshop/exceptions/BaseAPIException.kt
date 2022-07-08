package io.github.t3m8ch.springshop.exceptions

import io.github.t3m8ch.springshop.dto.APIErrorBody
import org.springframework.http.HttpStatus

abstract class BaseAPIException(
    val httpStatus: HttpStatus,
    val body: APIErrorBody,
) : RuntimeException(body.description)
