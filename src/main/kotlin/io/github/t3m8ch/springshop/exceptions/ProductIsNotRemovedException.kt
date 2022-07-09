package io.github.t3m8ch.springshop.exceptions

import io.github.t3m8ch.springshop.dto.APIErrorBody
import org.springframework.http.HttpStatus
import java.util.*

class ProductIsNotRemovedException(id: UUID) : BaseAPIException(
    HttpStatus.CONFLICT,
    APIErrorBody(
        "PRODUCT_NOT_REMOVED",
        "Product with ID = $id is not removed"
    ),
)
