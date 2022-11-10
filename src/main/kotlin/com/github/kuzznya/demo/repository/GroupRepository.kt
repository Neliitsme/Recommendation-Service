package com.github.kuzznya.demo.repository

import com.github.kuzznya.demo.entity.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GroupRepository : JpaRepository<GroupEntity, UUID> {
    fun findByName(name: String): GroupEntity?
    fun deleteByName(name: String)
}