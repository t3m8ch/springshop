package io.github.t3m8ch.springshop.exceptions.product

import io.github.t3m8ch.springshop.dto.APIErrorBody
import io.github.t3m8ch.springshop.exceptions.BaseAPIException
import org.springframework.http.HttpStatus
import java.util.*

class ProductNotFoundException(id: UUID) : BaseAPIException(
    HttpStatus.NOT_FOUND,
    APIErrorBody(
        ERROR_CODE,
        "Product with ID = $id not found"
    ),
) {
    companion object { const val ERROR_CODE = "PRODUCT_NOT_FOUND" }
}
