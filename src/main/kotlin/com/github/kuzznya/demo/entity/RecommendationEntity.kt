package com.github.kuzznya.demo.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "recommendations")
class RecommendationEntity {
    @Id
    @GeneratedValue
    var id: UUID? = null

    var userId: UUID? = null

    @OneToMany(mappedBy = "recommendation")
    var seenItemEntities: MutableList<SeenItemEntity>? = null

    @OneToMany(mappedBy = "recommendation")
    var seenCategoryEntities: MutableList<SeenCategoryEntity>? = null

}