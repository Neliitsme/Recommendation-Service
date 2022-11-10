package com.github.kuzznya.demo.service

import com.github.kuzznya.demo.entity.StudentEntity
import com.github.kuzznya.demo.model.Student
import com.github.kuzznya.demo.repository.GroupRepository
import org.springframework.stereotype.Component

@Component
class StudentMapper(
    private val groupRepository: GroupRepository
) {

    fun modelToEntity(model: Student): StudentEntity {
        val group = model.group?.let {
            groupRepository.findByName(it) ?:
            throw IllegalArgumentException("Group with name ${model.group} not found")
        }
        val entity = StudentEntity()
        entity.id = model.id
        entity.name = model.name
        entity.surname = model.surname
        entity.group = group
        entity.expelled = model.expelled
        return entity
    }

    fun entityToModel(entity: StudentEntity): Student {
        return Student(
            entity.id,
            entity.name!!,
            entity.surname!!,
            entity.group?.name,
            entity.expelled
        )
    }
}