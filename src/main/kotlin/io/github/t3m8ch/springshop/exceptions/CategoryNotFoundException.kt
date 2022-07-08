package io.github.t3m8ch.springshop.exceptions

import org.springframework.http.HttpStatus
import java.util.*

class CategoryNotFoundException(id: UUID) : BaseAPIException(
    HttpStatus.NOT_FOUND,
    APIErrorBody(
        "CATEGORY_NOT_FOUND",
        "Category with ID = $id not found"
    )
)
