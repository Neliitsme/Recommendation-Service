package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "student")
class StudentEntity {
    @Id
    @GeneratedValue
    var id: UUID? = null
    var name: String? = null
    var surname: String? = null
    var expelled = false

    @ManyToOne
    @JoinColumn(name = "group_id")
    var group: GroupEntity? = null
}