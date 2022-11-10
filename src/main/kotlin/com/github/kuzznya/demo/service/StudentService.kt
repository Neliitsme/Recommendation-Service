package com.github.kuzznya.demo.service

import com.github.kuzznya.demo.model.Student
import com.github.kuzznya.demo.repository.StudentRepository
import com.github.kuzznya.demo.util.OffsetBasedPageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.transaction.Transactional

@Service
class StudentService(
    private val mapper: StudentMapper,
    private val repository: StudentRepository
) {
    @Transactional
    fun createStudent(student: Student): Student {
        val entity = try {
            mapper.modelToEntity(student)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
        return mapper.entityToModel(repository.save(entity))
    }

    fun getStudents(limit: Int, offset: Int): List<Student> =
        repository.findAll(OffsetBasedPageRequest(offset, limit, Sort.by("surname")))
            .map { mapper.entityToModel(it) }
            .toList()

    fun findStudent(id: UUID): Student? = repository.findById(id)
        .orElse(null)?.let { mapper.entityToModel(it) }

    @Transactional
    fun expelStudent(id: UUID): Student? {
        val entity = repository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id $id not found") }
        entity.expelled = true
        entity.group = null
        return mapper.entityToModel(repository.save(entity))
    }

    fun deleteStudent(id: UUID) = repository.deleteById(id)
}