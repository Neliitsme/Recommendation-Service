package com.github.kuzznya.demo.controller

import com.github.kuzznya.demo.model.Group
import com.github.kuzznya.demo.service.GroupService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/groups")
class GroupController(
    private val service: GroupService
) {

    @GetMapping
    fun getGroups(): List<String> =
        service.getGroups()

    @GetMapping("/{name}")
    fun getGroup(@PathVariable("name") name: String): Group =
        service.findGroup(name) ?:
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "Group with name $name not found")

    @PostMapping
    fun createGroup(@RequestBody @Validated group: Group): Group =
        service.createGroup(group)

    @DeleteMapping("/{name}")
    fun deleteGroup(@PathVariable("name") name: String) =
        service.deleteGroup(name)
}