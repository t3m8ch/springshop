package io.github.t3m8ch.springshop.exceptions

import org.springframework.http.HttpStatus
import java.util.*

class CategoryIsRemovedException(id: UUID) : BaseAPIException(
    HttpStatus.LOCKED,
    APIErrorBody(
        errorCode = "CATEGORY_REMOVED",
        description = "Category with ID = $id is removed"
    )
)