package io.github.t3m8ch.springshop.controllers

import io.github.t3m8ch.springshop.dto.APIErrorBody
import io.github.t3m8ch.springshop.exceptions.BaseAPIException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BaseAPIException::class)
    fun handleBaseAPIException(ex: BaseAPIException): ResponseEntity<APIErrorBody> {
        return ResponseEntity(ex.body, ex.httpStatus)
    }
}
