package io.github.t3m8ch.springshop.exceptions

import io.github.t3m8ch.springshop.dto.APIErrorBody
import org.springframework.http.HttpStatus
import java.util.*

class ProductNotFoundException(id: UUID) : BaseAPIException(
    HttpStatus.NOT_FOUND,
    APIErrorBody(
        "PRODUCT_NOT_FOUND",
        "Product with ID = $id not found"
    ),
)
