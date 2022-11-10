package com.github.kuzznya.demo.repository

import com.github.kuzznya.demo.entity.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StudentRepository : JpaRepository<StudentEntity, UUID>
