package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "university_group") // group is a reserved keyword
class GroupEntity {
    @Id
    @GeneratedValue
    var id: UUID? = null

    @Column(unique = true)
    var name: String? = null

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "group")
    var students: List<StudentEntity>? = null
}