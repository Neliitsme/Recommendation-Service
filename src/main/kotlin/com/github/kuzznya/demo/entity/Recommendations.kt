package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import kotlin.collections.List

@Entity
@Table(name = "recommendations")
class RecommendationsEntity {
    @Id
    @GeneratedValue
    var id: UUID? = null

    var userId: UUID? = null

    @OneToMany
    var seenItems: List<SeenItems>? = null

    @OneToMany
    var seenCategories: List<SeenCategories>? = null

}