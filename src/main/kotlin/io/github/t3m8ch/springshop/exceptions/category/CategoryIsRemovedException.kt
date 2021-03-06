package io.github.t3m8ch.springshop.exceptions.category

import io.github.t3m8ch.springshop.dto.APIErrorBody
import io.github.t3m8ch.springshop.exceptions.BaseAPIException
import org.springframework.http.HttpStatus
import java.util.*

class CategoryIsRemovedException(id: UUID) : BaseAPIException(
    HttpStatus.LOCKED,
    APIErrorBody(
        errorCode = ERROR_CODE,
        description = "Category with ID = $id is removed"
    )
) {
    companion object { const val ERROR_CODE = "CATEGORY_REMOVED" }
}