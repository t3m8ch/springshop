package io.github.t3m8ch.springshop.exceptions

import io.github.t3m8ch.springshop.dto.APIErrorBody
import org.springframework.http.HttpStatus
import java.util.*

class ProductIsRemovedException(id: UUID) : BaseAPIException(
    HttpStatus.LOCKED,
    APIErrorBody(
        "PRODUCT_REMOVED",
        "Product with ID = $id is removed"
    ),
)
