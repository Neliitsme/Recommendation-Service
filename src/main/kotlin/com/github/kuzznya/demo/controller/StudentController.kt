package com.github.kuzznya.demo.controller

import com.github.kuzznya.demo.model.Student
import com.github.kuzznya.demo.service.StudentService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/v1/students")
class StudentController(
    private val service: StudentService
) {

    @PostMapping
    fun createStudent(@RequestBody @Validated student: Student) = service.createStudent(student)

    @GetMapping
    fun getStudents(
        @RequestParam(value = "limit", required = false, defaultValue = "100") limit: Int,
        @RequestParam(value = "offset", required = false, defaultValue = "0") offset: Int
    ) = service.getStudents(limit, offset)

    @GetMapping("/{id}")
    fun findStudent(@PathVariable("id") id: UUID): Student =
        service.findStudent(id) ?:
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id $id not found")

    @PostMapping("/{id}/actions/expel")
    fun expelStudent(@PathVariable("id") id: UUID): Student? = service.expelStudent(id)

    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable("id") id: UUID) = service.deleteStudent(id)
}