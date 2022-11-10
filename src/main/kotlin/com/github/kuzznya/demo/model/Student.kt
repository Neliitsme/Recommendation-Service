package com.github.kuzznya.demo.model

import java.util.UUID

data class Student(
    val id: UUID?,
    val name: String,
    val surname: String,
    val group: String?,
    val expelled: Boolean
)
