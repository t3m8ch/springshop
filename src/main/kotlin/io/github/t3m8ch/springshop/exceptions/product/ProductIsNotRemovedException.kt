package io.github.t3m8ch.springshop.exceptions.product

import io.github.t3m8ch.springshop.dto.APIErrorBody
import io.github.t3m8ch.springshop.exceptions.BaseAPIException
import org.springframework.http.HttpStatus
import java.util.*

class ProductIsNotRemovedException(id: UUID) : BaseAPIException(
    HttpStatus.CONFLICT,
    APIErrorBody(
        ERROR_CODE,
        "Product with ID = $id is not removed"
    ),
) {
    companion object { const val ERROR_CODE = "PRODUCT_NOT_REMOVED" }
}
